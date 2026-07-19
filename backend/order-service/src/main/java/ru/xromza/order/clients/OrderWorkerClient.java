package ru.xromza.order.clients;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.dto.OrderResponseDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderWorkerClient {
    private final RestClient orderWorkerRestClient;

    @CircuitBreaker(name = "orderWorkerService", fallbackMethod = "fetchOrdersFallback")
    @Retry(name = "orderWorkerService")
    public List<OrderResponseDto> fetchOrdersFromWorker(String orderId) {
        log.info("Отправил запрос на получение заказа orderId = {}", orderId);
        ServiceExchange orderExchange = orderWorkerRestClient.get().uri("/api/v1/order-worker/orders/" + orderId)
                .retrieve().body(ServiceExchange.class);
        log.info("Получил ответ для orderId = {}", orderId);
        return orderExchange.getOrder();
    }

    public List<OrderResponseDto> fetchOrdersFallback(String orderId, Throwable e) {
        log.error("Сервис order-worker недоступен: {}", e.getMessage());
        return List.of();
    }

    @CircuitBreaker(name = "orderWorkerService", fallbackMethod = "fetchOrdersFallback")
    @Retry(name = "orderWorkerService")
    public List<OrderResponseDto> fetchOrdersFromWorkerByUserId(Long userId) {
        log.info("Отправил запрос на получение заказов userId = {}", userId);
        ServiceExchange orderExchange = orderWorkerRestClient.get().uri("/api/v1/order-worker/orders")
                .header("X-User-Id", userId.toString()).retrieve().body(ServiceExchange.class);
        log.info("Получил ответ для userId = {}", userId);
        return orderExchange.getOrder();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ServiceExchange {
        public List<OrderResponseDto> order;
    }
}
