package ru.xromza.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.dto.ItemWarehouseAvailabilityResponseDto;
import ru.xromza.order.dto.OrderItemRequestDto;
import ru.xromza.order.dto.OrderPreviewRequestDto;
import ru.xromza.order.dto.OrderRequestDto;
import ru.xromza.order.dto.OrderResponseDto;
import ru.xromza.order.dto.OrderStatusHistoryResponseDto;
import ru.xromza.order.dto.PreOrderItemResponseDto;
import ru.xromza.order.dto.PreOrderPriceResponseDto;
import ru.xromza.order.dto.PreOrderResponseDto;
import ru.xromza.order.dto.PreOrderWarehouseResponseDto;
import ru.xromza.order.dto.ProductMinimalDto;
import ru.xromza.order.dto.ProductVariantOrderDto;
import ru.xromza.order.dto.StockResponseDto;
import ru.xromza.order.event.OrderSubmitEvent;
import ru.xromza.order.exceptions.ForbiddenException;
import ru.xromza.order.interfaces.ProductInfoInterface;
import ru.xromza.order.interfaces.ProductItemsInterface;
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
    private final RestClient warehouseRestClient;

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

    private <T extends ProductItemsInterface<I>, I extends ProductInfoInterface> List<T> fillProductFields(
            List<T> initialOrders) {

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
        for (T order : initialOrders) {
            List<I> items = order.getItems();
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

    public PreOrderResponseDto getOrderPreview(OrderPreviewRequestDto dto) {
        List<Long> variantIds = dto.getItems().stream().map(OrderItemRequestDto::getVariantId).toList();
        log.info("Отправляю запрос на получение остатков");
        List<StockResponseDto> stocks = warehouseRestClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("variantIds", variantIds)
                        .path("/api/v1/internal/stock")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockResponseDto>>() {
                });
        log.info("Запросил цены товаров у каталога: {}", variantIds.toString());
        Map<Long, ProductMinimalDto> minimalDtos = catalogRestClient.get().uri(
                uriBuilder -> uriBuilder.path("/api/v1/catalog/products/prices")
                        .queryParam("variantIds", variantIds).build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<Long, ProductMinimalDto>>() {
                });
        log.info("Получил цены товаров у каталога: {}", variantIds.toString());
        BigDecimal totalPrice = BigDecimal.ZERO;
        PreOrderResponseDto totalResponse = PreOrderResponseDto.builder().build();
        Map<Long, Integer> requestedQuantities = dto.getItems().stream()
                .collect(Collectors.toMap(
                        OrderItemRequestDto::getVariantId,
                        OrderItemRequestDto::getQuantity));

        Map<Long, List<StockResponseDto>> stocksByWarehouse = stocks.stream()
                .collect(Collectors.groupingBy(StockResponseDto::getWarehouseId));

        List<PreOrderWarehouseResponseDto> warehouses = stocksByWarehouse.entrySet().stream()
                .map(entry -> {
                    Long warehouseId = entry.getKey();
                    List<StockResponseDto> warehouseStocks = entry.getValue();

                    boolean isAvailableForOrder = requestedQuantities.entrySet().stream()
                            .allMatch(request -> {
                                Optional<StockResponseDto> stockOpt = warehouseStocks.stream()
                                        .filter(s -> s.getVariantId().equals(request.getKey()))
                                        .findFirst();

                                return stockOpt.isPresent() && stockOpt.get().getQuantity() >= request.getValue();
                            });

                    String address = warehouseStocks.isEmpty() ? "" : warehouseStocks.get(0).getAddress();

                    return PreOrderWarehouseResponseDto.builder()
                            .id(warehouseId)
                            .address(address)
                            .isAvailableForOrder(isAvailableForOrder)
                            .build();
                })
                .toList();
        for (OrderItemRequestDto item : dto.getItems()) {
            ProductMinimalDto productMinimalDto = minimalDtos.get(item.getVariantId());
            boolean isWholesale = item.getQuantity() >= productMinimalDto.wholesaleThreshold();
            BigDecimal appliedPrice = isWholesale
                    ? productMinimalDto.priceWholesale()
                    : productMinimalDto.priceRetail();
            BigDecimal subtotal = appliedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(subtotal);
            String priceType = isWholesale ? "WHOLESALE" : "RETAIL";
            PreOrderPriceResponseDto price = PreOrderPriceResponseDto.builder()
                    .base(productMinimalDto.priceRetail())
                    .applied(appliedPrice)
                    .type(priceType)
                    .build();

            List<StockResponseDto> variantStocks = stocks.stream()
                    .filter(stock -> stock.getVariantId() == item.getVariantId()).toList();

            int maxAvailable = variantStocks.stream()
                    .mapToInt(StockResponseDto::getQuantity)
                    .max()
                    .orElse(0);
            List<ItemWarehouseAvailabilityResponseDto> availableAtWarehouses = variantStocks.stream()
                    .map(stock -> ItemWarehouseAvailabilityResponseDto.builder()
                            .warehouseId(stock.getWarehouseId())
                            .availableQuantity(stock.getQuantity())
                            .build())
                    .toList();
            boolean isAvailable = variantStocks.stream()
                    .filter(stock -> stock.getWarehouseId() == dto.getWarehouseId())
                    .anyMatch(stock -> stock.getQuantity() >= item.getQuantity());
            PreOrderItemResponseDto itemResponse = PreOrderItemResponseDto.builder()
                    .variantId(item.getVariantId())
                    .price(price)
                    .maxAvailableQuantity(maxAvailable)
                    .availableAtWarehouses(availableAtWarehouses)
                    .quantity(item.getQuantity())
                    .subtotal(subtotal)
                    .isAvailable(isAvailable)
                    .build();
            totalResponse.addItem(itemResponse);
        }
        totalResponse.setTotalPrice(totalPrice);
        totalResponse.setWarehouses(warehouses);
        fillProductFields(List.of(totalResponse));
        return totalResponse;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ServiceExchange {
        public List<OrderResponseDto> order;
    }
}
