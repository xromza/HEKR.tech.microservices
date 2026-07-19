package ru.xromza.order.clients;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.event.OrderSubmitEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQClient {
    private final RabbitTemplate rabbitTemplate;

    @CircuitBreaker(name = "rabbitMqService", fallbackMethod = "sendOrderEventFallback")
    @Retry(name = "rabbitMqService")
    public Void sendOrderEvent(OrderSubmitEvent event) {
        rabbitTemplate.convertAndSend("order.submission.exchange", "order.submitted", event);
        return null;
    }

    public Void sendOrderEventFallback(OrderSubmitEvent event, Throwable e) {
        log.error("Не удалось отправить событие заказа {}: {}", event.getOrderId(), e.getMessage());
        throw new RuntimeException("Не удалось создать заказ. Попробуйте позже.", e);
    }
}
