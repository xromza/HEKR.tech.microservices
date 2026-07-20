package ru.xromza.order.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.event.OrderStatusUpdatedEvent;
import ru.xromza.order.service.OrderStatusService;

@RabbitListener(queues = "order.status.queue")
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusUpdatedListener {
    private final OrderStatusService orderStatusService;

    @RabbitHandler
    public void handleOrderStatusUpdated(OrderStatusUpdatedEvent event) {
        log.info("Статус заказа изменился {}: -> {}", event.getOrderId(), event.getStatus());
        orderStatusService.changeOrderStatus(event.getOrderId(), event.getStatus());
        log.info("Положил в Redis {}: -> {}", event.getOrderId(), event.getStatus());
    }
}
