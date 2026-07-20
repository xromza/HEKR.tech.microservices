package ru.xromza.order_worker.clients;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.xromza.order_worker.dto.ProductMinimalDto;
import ru.xromza.order_worker.exceptions.CatalogUnavailableException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogClient {
    private final RestClient catalogRestClient;

    @CircuitBreaker(name = "catalogService", fallbackMethod = "fetchPricesFromCatalogFallback")
    @Retry(name = "catalogService")
    public Map<Long, ProductMinimalDto> fetchPricesFromCatalog(List<Long> variantIds) throws CatalogUnavailableException {
        log.info("Запросил цены товаров у каталога: {}", variantIds.toString());
        Map<Long, ProductMinimalDto> minimalDtos = catalogRestClient.get().uri(
                uriBuilder -> uriBuilder.path("/api/v1/catalog/products/prices")
                        .queryParam("variantIds", variantIds).build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<Long, ProductMinimalDto>>() {
                });
        log.info("Получил цены товаров у каталога: {}", variantIds.toString());
        return minimalDtos;
    }

    public Map<Long, ProductMinimalDto> fetchPricesFromCatalogFallback(List<Long> variantIds, Throwable e) throws CatalogUnavailableException {
        log.error("Сервис каталога недоступен: {}", e.getMessage());
        throw new CatalogUnavailableException("Не удалось получить цены из каталога", variantIds);
    }
}
