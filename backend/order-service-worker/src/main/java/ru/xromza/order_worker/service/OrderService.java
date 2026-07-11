package ru.xromza.order_worker.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import ru.xromza.order_worker.dto.OrderResponseDto;
import ru.xromza.order_worker.dto.OrderStatusHistoryResponseDto;
import ru.xromza.order_worker.dto.ProductMinimalDto;
import ru.xromza.order_worker.event.OrderCreatedEvent;
import ru.xromza.order_worker.event.OrderCreatedInternalEvent;
import ru.xromza.order_worker.event.OrderSubmitEvent;
import ru.xromza.order_worker.exceptions.NotFoundException;
import ru.xromza.order_worker.mapper.OrderItemMapper;
import ru.xromza.order_worker.mapper.OrderMapper;
import ru.xromza.order_worker.mapper.OrderResponseMapper;
import ru.xromza.order_worker.mapper.OrderStatusHistoryMapper;
import ru.xromza.order_worker.model.Order;
import ru.xromza.order_worker.model.OrderItem;
import ru.xromza.order_worker.model.OrderItemEventModel;
import ru.xromza.order_worker.model.OrderItemId;
import ru.xromza.order_worker.model.OrderStatusHistory;
import ru.xromza.order_worker.repository.OrderRepository;
import ru.xromza.order_worker.repository.OrderStatusHistoryRepository;
import ru.xromza.order_worker.utils.PaymentMethod;
import ru.xromza.order_worker.utils.Status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Qualifier("catalogRestClient")
    private final RestClient catalogRestClient;

    @Qualifier("userRestClient")
    private final RestClient userRestClient;

    private Order findById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrders(Long userId) {
        return orderResponseMapper.toDtoList(orderRepository.findByUserIdVerbose(userId));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        Page<Order> order = orderRepository.findAll(pageable);
        return order.map(orderResponseMapper::toDto);
    }

    public OrderResponseDto getOrder(String id) {
        Order order = orderRepository.findByIdWithItemsAndHistory(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));

        return orderResponseMapper.toDto(order);
    }

    @Transactional
    public OrderResponseDto createOrder(OrderSubmitEvent event) {
        List<OrderItemEventModel> items = event.getItems();

        log.info("Начал создание заказа для orderId = {}", event.getOrderId());

        List<Long> variantIds = items.stream().map(item -> item.getVariantId()).toList();
        log.info("Запросил цены товаров у каталога: {}", variantIds.toString());
        Map<Long, ProductMinimalDto> minimalDtos = catalogRestClient.get().uri(
                uriBuilder -> uriBuilder.path("/api/v1/catalog/products/prices")
                        .queryParam("variantIds", variantIds).build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<Long, ProductMinimalDto>>() {
                });
        log.info("Получил цены товаров у каталога: {}", variantIds.toString());
        Order order = orderMapper.toOrder(event);
        BigDecimal orderTotal = BigDecimal.ZERO;
        log.info("Собираю товары");
        for (OrderItemEventModel item : items) {
            OrderItemId orderItemId = new OrderItemId(event.getOrderId(), item.getVariantId());
            ProductMinimalDto minimalDto = minimalDtos.get(item.getVariantId());
            boolean isWholesale = item.getQuantity() >= minimalDto.wholesaleThreshold();
            BigDecimal appliedPrice = isWholesale
                    ? minimalDto.priceWholesale()
                    : minimalDto.priceRetail();

            BigDecimal subtotal = appliedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            OrderItem orderItem = OrderItem.builder()
                    .id(orderItemId)
                    .quantity(item.getQuantity())
                    .priceAtPurchase(appliedPrice)
                    .totalPrice(subtotal)
                    .priceType(isWholesale ? "WHOLESALE" : "RETAIL")
                    .build();
            orderTotal = orderTotal.add(subtotal);
            order.addItem(orderItem);
        }
        order.setStatus(Status.NEW);
        order.setUserId(event.getUserId());
        order.setPayment(PaymentMethod.valueOf(event.getPaymentMethod()));
        order.setPrice(orderTotal);
        order.setWarehouseId(event.getWarehouseId());
        order.setDate(LocalDateTime.now());
        String statusHistoryUUID = UUID.randomUUID().toString();
        OrderStatusHistory orderStatusHistory = OrderStatusHistory.builder()
                .id(statusHistoryUUID)
                .changedBy(0L)
                .newStatus(Status.NEW)
                .changedAt(LocalDateTime.now())
                .order(order)
                .comment("Заказ создан")
                .build();
        order.addHistory(orderStatusHistory);
        Order saved = orderRepository.save(order);
        OrderCreatedEvent warehouseEvent = OrderCreatedEvent.builder()
                .warehouseId(event.getWarehouseId())
                .orderId(event.getOrderId())
                .items(orderItemMapper.toEventItemsDto(event.getItems()))
                .build();

        eventPublisher.publishEvent(new OrderCreatedInternalEvent(warehouseEvent));
        log.info("Послал событие создания заказа {}", event.getOrderId());
        return orderResponseMapper.toDto(saved);
    }

    @Transactional
    public OrderStatusHistoryResponseDto updateStatus(Long changedByUserId, String orderId, Status status,
            String comment) {
        Order order = findById(orderId);
        String uuidStatus = UUID.randomUUID().toString();
        OrderStatusHistory orderStatusHistory = OrderStatusHistory.builder()
                .id(uuidStatus)
                .newStatus(status)
                .changedAt(LocalDateTime.now())
                .changedBy(changedByUserId)
                .comment(comment)
                .order(order)
                .build();

        order.setStatus(status);
        orderRepository.save(order);
        return orderStatusHistoryMapper.toDto(orderStatusHistoryRepository.save(orderStatusHistory));
    }
}