package ru.xromza.order_worker.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order_worker.event.OrderSubmitEvent;
import ru.xromza.order_worker.service.OrderService;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "order.submission.queue")
public class OrderSubmittedListener {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @RabbitHandler
    public void handleOrderSubmitted(OrderSubmitEvent event) {
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(event);
        log.info("Получен новый заказ: {}", prettyJson);
        orderService.createOrder(event);
    }
}
