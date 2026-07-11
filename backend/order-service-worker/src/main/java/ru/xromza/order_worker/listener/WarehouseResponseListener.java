package ru.xromza.order_worker.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order_worker.event.InventoryResultEvent;
import ru.xromza.order_worker.service.OrderService;
import ru.xromza.order_worker.utils.Status;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "order.warehouse-responses.queue")
public class WarehouseResponseListener {
    private final OrderService orderService;

    @RabbitHandler
    public void handleOrder(InventoryResultEvent event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        switch (routingKey) {
            case "inventory.reserved" -> handleSuccess(event, routingKey);
            case "inventory.not_enough_items" -> handleNotEnoughItems(event);
            default -> log.warn("Пришёл неизвестный ключ: {} для заказа {}", routingKey, event.getOrderId());
        }
    }

    public void handleSuccess(InventoryResultEvent event, String routingKey) {
        log.info("Склад успешно зарезервировал товары для заказа: {}. Ключ: {}", event.getOrderId(), routingKey);
        orderService.updateStatus(0L, event.getOrderId(), Status.ASSEMBLING, "Заказ подтверждён");
    }

    public void handleNotEnoughItems(InventoryResultEvent event) {
        log.warn("На складе не хватило товаров для заказа: {}. Причина: {}", event.getOrderId(), event.getReason());
        log.warn("Список проблемных позиций: {}", event.getItemsError());

        orderService.updateStatus(0L, event.getOrderId(), Status.CANCELED, "Не хватает товаров для заказа");
    }

}
