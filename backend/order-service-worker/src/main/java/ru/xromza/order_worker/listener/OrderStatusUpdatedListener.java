package ru.xromza.order_worker.listener;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order_worker.event.OrderStatusUpdatedInternalEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusUpdatedListener {
    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderStatusUpdated(OrderStatusUpdatedInternalEvent event) {
        rabbitTemplate.convertAndSend(
                "order.status.exchange",
                "order.status.updated",
                event.payload());
        log.info("Отправлено сообщение сервису заказов об обновлении статуса orderId = {}",
                event.payload().getOrderId());
    }
}
