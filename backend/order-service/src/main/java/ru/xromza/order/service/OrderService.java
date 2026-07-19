package ru.xromza.order.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.clients.CatalogClient;
import ru.xromza.order.clients.OrderWorkerClient;
import ru.xromza.order.clients.RabbitMQClient;
import ru.xromza.order.clients.UserClient;
import ru.xromza.order.clients.WarehouseClient;
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
    private final CatalogClient catalogClient;
    private final OrderWorkerClient orderWorkerClient;
    private final WarehouseClient warehouseClient;
    private final UserClient userClient;
    private final RabbitMQClient rabbitMQClient;

    public String createOrder(OrderRequestDto dto, Long userId) {
        String uuid = UUID.randomUUID().toString();

        OrderSubmitEvent event = OrderSubmitEvent.builder()
                .orderId(uuid)
                .address(dto.getAddress())
                .comment(dto.getComment())
                .items(orderItemMapper.toEventItemsList(dto.getItems()))
                .warehouseId(dto.getWarehouseId())
                .paymentMethod(dto.getPayment())
                .userId(userId).build();
        rabbitMQClient.sendOrderEvent(event);

        return uuid;
    }

    public OrderResponseDto getOrder(String orderId, Long userId) {
        List<OrderResponseDto> orders = orderWorkerClient.fetchOrdersFromWorker(orderId);
        if (orders == null || orders.isEmpty()) {
            throw new RuntimeException("Заказы не найдены");
        }
        OrderResponseDto order = orders.getFirst();
        if (order.getUserId() != userId) {
            throw new ForbiddenException("У вас нет прав для просмотра этого заказа");
        }
        fillProductFields(orders);
        fillChangedByNameInStatusHistory(orders);

        return order;
    }

    private <T extends ProductItemsInterface<I>, I extends ProductInfoInterface> List<T> fillProductFields(
            List<T> initialOrders) {

        List<Long> variantIds = initialOrders.stream().flatMap(item -> item.getItems().stream())
                .map(item -> item.getVariantId()).distinct().toList();
        Map<Long, ProductVariantOrderDto> orderItemsData = catalogClient.fetchOrderItemsData(variantIds);
        for (T order : initialOrders) {
            List<I> items = order.getItems();
            if (items == null || items.isEmpty()) {
                continue;
            }
            order.setItems(items.stream().map(item -> {
                ProductVariantOrderDto orderData = orderItemsData.get(item.getVariantId());
                if (orderData != null) {
                    item.setBrand(orderData.getBrand());
                    item.setColor(orderData.getColor());
                    item.setMainImageUrl(orderData.getMainImageUrl());
                    item.setSize(orderData.getSize());
                    item.setTitle(orderData.getTitle());
                    item.setProductId(orderData.getProductId());
                    item.setSku(orderData.getSku());
                }
                return item;
            }).toList());
        }
        return initialOrders;
    }

   

    private void fillChangedByNameInStatusHistory(List<OrderResponseDto> initialOrders) {
        log.info("Заполняю имена пользователей из истории статусов");
        List<Long> userIds = initialOrders.stream().flatMap(historyEntry -> historyEntry.getStatusHistory().stream())
                .map(OrderStatusHistoryResponseDto::getChangedById).distinct().toList();
        Map<Long, String> usernames = userClient.getUsernames(userIds);
        for (OrderResponseDto order : initialOrders) {
            List<OrderStatusHistoryResponseDto> history = order.getStatusHistory();
            if (history != null) {
                for (OrderStatusHistoryResponseDto historyEntry : history) {
                    historyEntry.setChangedByName(usernames.getOrDefault(historyEntry.getChangedById(), "Unknown"));
                }
            }
        }
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        List<OrderResponseDto> orders = orderWorkerClient.fetchOrdersFromWorkerByUserId(userId);
        log.info("Запрашиваю информацию о товарах из каталога для клиента userId = {}", userId);
        fillProductFields(orders);
        fillChangedByNameInStatusHistory(orders);
        return orders;
    }

    public PreOrderResponseDto getOrderPreview(OrderPreviewRequestDto dto) {
        List<Long> variantIds = dto.getItems().stream()
                .map(OrderItemRequestDto::getVariantId)
                .distinct()
                .toList();

        List<StockResponseDto> stocks = warehouseClient.fetchStocksByVariantIds(variantIds);
        Map<Long, List<StockResponseDto>> stocksByVariant = stocks.stream()
                .collect(Collectors.groupingBy(StockResponseDto::getVariantId));

        Map<Long, ProductMinimalDto> prices = catalogClient.fetchPricesFromCatalog(variantIds);

        BigDecimal totalPrice = BigDecimal.ZERO;

        PreOrderResponseDto totalResponse = PreOrderResponseDto.builder().build();
        Map<Long, Integer> requestedQuantities = dto.getItems().stream()
                .collect(Collectors.toMap(OrderItemRequestDto::getVariantId, OrderItemRequestDto::getQuantity));

        Map<Long, List<StockResponseDto>> stocksByWarehouse = stocks.stream()
                .collect(Collectors.groupingBy(StockResponseDto::getWarehouseId));

        List<PreOrderWarehouseResponseDto> warehouses = stocksByWarehouse.entrySet().stream().map(entry -> {
            Long warehouseId = entry.getKey();

            List<StockResponseDto> warehouseStocks = entry.getValue();
            boolean isAvailableForOrder = requestedQuantities.entrySet().stream().allMatch(request -> {
                Optional<StockResponseDto> stockOpt = warehouseStocks.stream()
                        .filter(s -> s.getVariantId().equals(request.getKey()))
                        .findFirst();

                return stockOpt.isPresent() && stockOpt.get().getQuantity() >= request.getValue();
            });

            String address = warehouseStocks.isEmpty() ? "" : warehouseStocks.get(0).getAddress();

            return PreOrderWarehouseResponseDto.builder().id(warehouseId).address(address)
                    .isAvailableForOrder(isAvailableForOrder).build();
        }).toList();
        for (OrderItemRequestDto item : dto.getItems()) {
            BigDecimal subtotal = BigDecimal.ZERO;
            PreOrderPriceResponseDto price = PreOrderPriceResponseDto.builder().build();
            if (prices != null && prices.containsKey(item.getVariantId())) {
                ProductMinimalDto productMinimalDto = prices.get(item.getVariantId());
                if (productMinimalDto.priceRetail() != null) {
                    boolean isWholesale = item.getQuantity() >= productMinimalDto.wholesaleThreshold();
                    BigDecimal appliedPrice = isWholesale ? productMinimalDto.priceWholesale()
                            : productMinimalDto.priceRetail();
                    subtotal = appliedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    totalPrice = totalPrice.add(subtotal);
                    String priceType = isWholesale ? "WHOLESALE" : "RETAIL";

                    price.setBase(productMinimalDto.priceRetail());
                    price.setApplied(appliedPrice);
                    price.setType(priceType);
                }
            }
            int maxAvailable = 0;
            boolean isAvailable = false;
            List<StockResponseDto> variantStocks = stocksByVariant.getOrDefault(item.getVariantId(),
                    Collections.emptyList());

            maxAvailable = variantStocks.stream().mapToInt(StockResponseDto::getQuantity).max().orElse(0);
            List<ItemWarehouseAvailabilityResponseDto> availableAtWarehouses = variantStocks
                    .stream()
                    .map(stock -> ItemWarehouseAvailabilityResponseDto.builder()
                            .warehouseId(stock.getWarehouseId())
                            .availableQuantity(stock.getQuantity())
                            .build())
                    .toList();
            isAvailable = variantStocks.stream()
                    .filter(stock -> stock.getWarehouseId().equals(dto.getWarehouseId()))
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

}
