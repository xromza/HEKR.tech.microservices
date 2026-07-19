package ru.xromza.order.clients;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order.dto.StockResponseDto;

@RequiredArgsConstructor
@Component
@Slf4j
public class WarehouseClient {
    private final RestClient warehouseRestClient;

    @CircuitBreaker(name = "warehouseService", fallbackMethod = "fetchStocksByVariantIdsFallback")
    @Retry(name = "warehouseService")
    public List<StockResponseDto> fetchStocksByVariantIds(List<Long> variantIds) {
        log.info("Отправляю запрос на получение остатков variantIds = {}", variantIds);
        List<StockResponseDto> stocks = warehouseRestClient.get().uri(
                uriBuilder -> uriBuilder.queryParam("variantIds", variantIds).path("/api/v1/internal/stock").build())
                .retrieve().body(new ParameterizedTypeReference<List<StockResponseDto>>() {
                });
        log.info("Остатки получены: {}", variantIds);
        return stocks;
    }

    public List<StockResponseDto> fetchStocksByVariantIdsFallback(List<Long> variantIds, Throwable e) {
        log.error("Ошибка при получении остатков {}: {}", variantIds.toString(), e.getMessage());
        return Collections.emptyList();
    }
}
