package ru.xromza.order_worker.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.order_worker.dto.ProductMinimalDto;
import ru.xromza.order_worker.clients.CatalogClient;
import ru.xromza.order_worker.dto.OrderResponseDto;
import ru.xromza.order_worker.dto.OrderStatusHistoryResponseDto;
import ru.xromza.order_worker.event.OrderCreatedEvent;
import ru.xromza.order_worker.event.OrderCreatedInternalEvent;
import ru.xromza.order_worker.event.OrderStatusUpdatedEvent;
import ru.xromza.order_worker.event.OrderStatusUpdatedInternalEvent;
import ru.xromza.order_worker.event.OrderSubmitEvent;
import ru.xromza.order_worker.exceptions.CatalogUnavailableException;
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
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final CatalogClient catalogClient;

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
    private OrderResponseDto saveCanceledOrder(OrderSubmitEvent event, String message) {
        log.error("Заказ отменяется по причине: {}", message);

        Order order = orderMapper.toOrder(event);
        for (OrderItemEventModel item : event.getItems()) {
            order.addItem(buildOrderItem(event.getOrderId(), item, null));
        }

        order.setUserId(event.getUserId());
        order.setPayment(PaymentMethod.valueOf(event.getPaymentMethod()));
        order.setPrice(null);
        order.setWarehouseId(event.getWarehouseId());
        order.setDate(LocalDateTime.now());

        changeStatus(order, Status.CANCELED, message);

        Order saved = orderRepository.save(order);
        return orderResponseMapper.toDto(saved);
    }

    private OrderResponseDto saveConfirmedOrder(OrderSubmitEvent event, Map<Long, ProductMinimalDto> prices) {
        Order order = orderMapper.toOrder(event);
        BigDecimal orderTotal = BigDecimal.ZERO;

        for (OrderItemEventModel item : event.getItems()) {
            ProductMinimalDto priceInfo = prices.get(item.getVariantId());
            if (priceInfo == null) {
                return saveCanceledOrder(event, "Не найдена цена для товара " + item.getVariantId());
            }
            OrderItem orderItem = buildOrderItem(event.getOrderId(), item, priceInfo);
            orderTotal = orderTotal.add(orderItem.getTotalPrice());
            order.addItem(orderItem);
        }

        order.setUserId(event.getUserId());
        order.setPayment(PaymentMethod.valueOf(event.getPaymentMethod()));
        order.setPrice(orderTotal);
        order.setWarehouseId(event.getWarehouseId());
        order.setDate(LocalDateTime.now());

        changeStatus(order, Status.NEW, "Заказ создан");

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

    private OrderItem buildOrderItem(String orderId, OrderItemEventModel item, ProductMinimalDto priceInfo) {
        OrderItemId orderItemId = new OrderItemId(orderId, item.getVariantId());

        if (priceInfo == null) {
            return OrderItem.builder()
                    .id(orderItemId)
                    .quantity(item.getQuantity())
                    .priceAtPurchase(null)
                    .totalPrice(null)
                    .priceType(null)
                    .build();
        }

        boolean isWholesale = item.getQuantity() >= priceInfo.wholesaleThreshold();
        BigDecimal appliedPrice = isWholesale ? priceInfo.priceWholesale() : priceInfo.priceRetail();
        BigDecimal subtotal = appliedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return OrderItem.builder()
                .id(orderItemId)
                .quantity(item.getQuantity())
                .priceAtPurchase(appliedPrice)
                .totalPrice(subtotal)
                .priceType(isWholesale ? "WHOLESALE" : "RETAIL")
                .build();
    }

    @Transactional
    public OrderResponseDto createOrder(OrderSubmitEvent event) {
        List<OrderItemEventModel> items = event.getItems();

        log.info("Начал создание заказа для orderId = {}", event.getOrderId());

        List<Long> variantIds = items.stream().map(item -> item.getVariantId()).toList();
        OrderResponseDto dto;
        try {
            Map<Long, ProductMinimalDto> minimalDtos = catalogClient.fetchPricesFromCatalog(variantIds);
            dto = saveConfirmedOrder(event, minimalDtos);
        } catch (CatalogUnavailableException ex) {
            dto = saveCanceledOrder(event, "Не удалось оформить заказ");
        }
        return dto;
    }

    private void changeStatus(Order order, Status newStatus, String comment) {
        changeStatus(order, newStatus, comment, 0L);
    }

    private OrderStatusHistory changeStatus(Order order, Status status, String comment, Long changedByUserId) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .id(UUID.randomUUID().toString())
                .changedBy(changedByUserId)
                .newStatus(status)
                .changedAt(LocalDateTime.now())
                .order(order)
                .comment(comment)
                .build();
        order.setStatus(status);
        order.addHistory(history);
        OrderStatusUpdatedEvent event = OrderStatusUpdatedEvent.builder()
                .orderId(order.getId())
                .status(status.name())
                .build();
        eventPublisher.publishEvent(new OrderStatusUpdatedInternalEvent(event));
        return history;
    }

    @Transactional
    public OrderStatusHistoryResponseDto updateStatus(Long changedByUserId, String orderId, Status status,
            String comment) {
        Order order = findById(orderId);
        OrderStatusHistory lastHistory = changeStatus(order, status, comment, changedByUserId);
        orderRepository.save(order);

        return orderStatusHistoryMapper.toDto(lastHistory);
    }
}