package ru.xromza.order.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.dto.OrderItemResponseDto;
import ru.xromza.order.dto.OrderRequestDto;
import ru.xromza.order.dto.OrderResponseDto;
import ru.xromza.order.dto.OrderStatusHistoryResponseDto;
import ru.xromza.order.dto.ProductVariantOrderDto;
import ru.xromza.order.event.OrderSubmitEvent;
import ru.xromza.order.exceptions.ForbiddenException;
import ru.xromza.order.mapper.OrderItemMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderItemMapper orderItemMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RestClient orderWorkerRestClient;
    private final RestClient catalogRestClient;
    private final RestClient userRestClient;

    public String createOrder(OrderRequestDto dto, Long userId) {
        String uuid = UUID.randomUUID().toString();

        OrderSubmitEvent event = OrderSubmitEvent.builder()
                .orderId(uuid)
                .address(dto.getAddress())
                .comment(dto.getComment())
                .items(orderItemMapper.toEventItemsList(dto.getItems()))
                .warehouseId(dto.getWarehouseId())
                .paymentMethod(dto.getPayment())
                .userId(userId)
                .build();
        rabbitTemplate.convertAndSend("order.submission.exchange", "order.submitted", event);

        return uuid;
    }

    public OrderResponseDto getOrder(String orderId, Long userId) {
        log.info("Отправил запрос на получение заказа orderId = ", orderId);
        ServiceExchange orderExchange = orderWorkerRestClient.get().uri("/api/v1/order-worker/orders/" + orderId)
                .retrieve()
                .body(ServiceExchange.class);
        log.info("Получил ответ для orderId = {}", orderId);
        List<OrderResponseDto> orders = orderExchange.getOrder();
        log.info("Запрашиваю информацию о товарах из каталога для заказа orderId = ", orderId);
        fillProductFields(orders);
        fillChangedByNameInStatusHistory(orders);
        OrderResponseDto order = orders.getFirst();

        if (order.getUserId() != userId) {
            throw new ForbiddenException("У вас нет прав для просмотра этого заказа");
        }

        return orders.getFirst();
    }

    private List<OrderResponseDto> fillProductFields(List<OrderResponseDto> initialOrders) {
        List<Long> variantIds = initialOrders.stream().flatMap(item -> item.getItems().stream())
                .map(item -> item.getVariantId()).distinct().toList();
        Map<Long, ProductVariantOrderDto> orderItemsData = catalogRestClient
                .get()
                .uri(uriBuilder -> uriBuilder.queryParam("variantIds", variantIds)
                        .path("/api/v1/catalog/products/items")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<Long, ProductVariantOrderDto>>() {
                });
        for (OrderResponseDto order : initialOrders) {
            List<OrderItemResponseDto> items = order.getItems();
            order.setItems(items.stream().map(item -> {
                ProductVariantOrderDto orderData = orderItemsData.get(item.getVariantId());
                item.setBrand(orderData.getBrand());
                item.setColor(orderData.getColor());
                item.setMainImageUrl(orderData.getMainImageUrl());
                item.setSize(orderData.getSize());
                item.setTitle(orderData.getTitle());
                item.setProductId(orderData.getProductId());
                item.setSku(orderData.getSku());
                return item;
            }).toList());
        }
        return initialOrders;
    }

    public Map<Long, String> getUsernames(List<Long> userIds) {
        log.info("Запрашиваю список имён пользователей по id: {}", userIds.toString());
        return userRestClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("ids", userIds)
                        .path("/api/v1/internal/user/name").build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<Long, String>>() {
                });
    }

    private void fillChangedByNameInStatusHistory(List<OrderResponseDto> initialOrders) {
        log.info("Заполняю имена пользователей из истории статусов");
        List<Long> userIds = initialOrders.stream().flatMap(historyEntry -> historyEntry.getStatusHistory().stream())
                .map(OrderStatusHistoryResponseDto::getChangedById).distinct().toList();
        Map<Long, String> usernames = getUsernames(userIds);
        for (OrderResponseDto order : initialOrders) {
            List<OrderStatusHistoryResponseDto> history = order.getStatusHistory();
            for (OrderStatusHistoryResponseDto historyEntry : history) {
                historyEntry.setChangedByName(usernames.get(historyEntry.getChangedById()));
            }
        }
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        log.info("Отправил запрос на получение всех заказов userId = {}", userId);
        ServiceExchange orderExchange = orderWorkerRestClient.get().uri("/api/v1/order-worker/orders")
                .header("X-User-Id", userId.toString())
                .retrieve()
                .body(ServiceExchange.class);
        log.info("Получил ответ для userId = {}", userId);
        List<OrderResponseDto> orders = orderExchange.getOrder();
        log.info("Запрашиваю информацию о товарах из каталога для клиента userId = {}", userId);
        fillProductFields(orders);
        fillChangedByNameInStatusHistory(orders);
        return orders;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ServiceExchange {
        public List<OrderResponseDto> order;
    }
}
