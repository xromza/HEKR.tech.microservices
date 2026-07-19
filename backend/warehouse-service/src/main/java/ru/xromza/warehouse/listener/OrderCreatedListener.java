package ru.xromza.warehouse.listener;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.warehouse.dto.OrderItemEventDto;
import ru.xromza.warehouse.event.InventoryResultEvent;
import ru.xromza.warehouse.event.OrderCreatedEvent;
import ru.xromza.warehouse.exceptions.NotEnoughItems;
import ru.xromza.warehouse.service.StockService;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {
    private final RabbitTemplate rabbitTemplate;
    private final StockService stockService;

    @RabbitListener(queues = "warehouse.order-submitted.queue")
    public void handleNewOrder(OrderCreatedEvent event) {
        List<OrderItemEventDto> items = event.getItems();
        boolean success = false;
        try {
            success = stockService.reserveItems(event.getWarehouseId(), items);
            if (success) {
                InventoryResultEvent successEvent = InventoryResultEvent.builder()
                        .orderId(event.getOrderId())
                        .status("SUCCESS")
                        .build();
                log.info("Успешно зарезервировал заказ: {}", event.getOrderId());
                rabbitTemplate.convertAndSend("warehouse.events.exchange", "inventory.reserved", successEvent);
                log.info("Отрправил inventory.reserved для заказа: {}", event.getOrderId());
            }
        } catch (NotEnoughItems ex) {
            InventoryResultEvent lowStockEvent = InventoryResultEvent.builder()
                    .orderId(event.getOrderId())
                    .status("NOT_ENOUGH_ITEMS")
                    .reason(ex.getMessage())
                    .itemsError(ex.getErrors())
                    .build();
            log.warn("Ошибка при резервации заказа: {}", event.getOrderId());
            System.out.println(ex.getErrors().toString());
            rabbitTemplate.convertAndSend("warehouse.events.exchange", "inventory.not_enough_items",
                    lowStockEvent);
        }

    }
}
