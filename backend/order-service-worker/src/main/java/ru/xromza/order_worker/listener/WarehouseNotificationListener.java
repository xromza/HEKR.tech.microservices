package ru.xromza.order_worker.listener;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order_worker.event.OrderCreatedInternalEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class WarehouseNotificationListener {
    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedInternalEvent event) {
        rabbitTemplate.convertAndSend(
                "warehouse.events.exchange",
                "order.created",
                event.payload());
        log.info("Отправлено сообщение складу о списании товаров заказа orderId = {}",
                event.payload().getOrderId());
    }
}
