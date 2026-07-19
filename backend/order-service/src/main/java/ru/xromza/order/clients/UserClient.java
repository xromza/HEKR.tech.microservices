package ru.xromza.order.clients;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserClient {
    private final RestClient userRestClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "getUsernamesFallback")
    @Retry(name = "userService")
    public Map<Long, String> getUsernames(List<Long> userIds) {
        log.info("Запрашиваю список имён пользователей по id: {}", userIds.toString());
        return userRestClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("ids", userIds).path("/api/v1/internal/user/name").build())
                .retrieve().body(new ParameterizedTypeReference<Map<Long, String>>() {
                });
    }

    public Map<Long, String> getUsernamesFallback(List<Long> userIds, Throwable e) {
        log.error("Сервис пользователей недоступен {}: {}", userIds.toString(), e.getMessage());
        return userIds.stream().collect(Collectors.toMap(key -> key, key -> "Unknown"));
    }
}
