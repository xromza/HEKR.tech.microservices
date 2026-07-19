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
import ru.xromza.order.dto.ProductMinimalDto;
import ru.xromza.order.dto.ProductVariantOrderDto;

@RequiredArgsConstructor
@Component
@Slf4j
public class CatalogClient {
    private final RestClient catalogRestClient;

    @Retry(name = "catalogService")
    @CircuitBreaker(name = "catalogService", fallbackMethod = "fetchOrderItemsFallback")
    public Map<Long, ProductVariantOrderDto> fetchOrderItemsData(List<Long> variantIds) {
        log.info("Запрашиваю информацию о товарах из каталога для товаров: {}", variantIds.toString());
        Map<Long, ProductVariantOrderDto> orderItemsData = catalogRestClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("variantIds", variantIds)
                        .path("/api/v1/catalog/products/items").build())
                .retrieve().body(new ParameterizedTypeReference<Map<Long, ProductVariantOrderDto>>() {
                });
        return orderItemsData;
    }

    public Map<Long, ProductVariantOrderDto> fetchOrderItemsFallback(List<Long> variantIds, Throwable e) {
        log.error("Сервис каталога недоступен {}: {}", variantIds.toString(), e.getMessage());
        Map<Long, ProductVariantOrderDto> data = variantIds.stream()
                .collect(Collectors.toMap(key -> key, key -> ProductVariantOrderDto.builder().build()));
        return data;
    }

    @CircuitBreaker(name = "catalogService", fallbackMethod = "fetchPricesFromCatalogFallback")
    @Retry(name = "catalogService")
    public Map<Long, ProductMinimalDto> fetchPricesFromCatalog(List<Long> variantIds) {
        log.info("Запросил цены товаров у каталога: {}", variantIds.toString());
        Map<Long, ProductMinimalDto> minimalDtos = catalogRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/catalog/products/prices")
                        .queryParam("variantIds", variantIds).build())
                .retrieve().body(new ParameterizedTypeReference<Map<Long, ProductMinimalDto>>() {
                });
        log.info("Получил цены товаров у каталога: {}", variantIds.toString());
        return minimalDtos;
    }

    public Map<Long, ProductMinimalDto> fetchPricesFromCatalogFallback(List<Long> variantIds, Throwable e) {
        log.error("Ошибка при получении цен с каталога {}: {}", variantIds.toString(), e.getMessage());
        return variantIds.stream()
                .collect(Collectors
                        .toMap(
                                key -> key,
                                key -> new ProductMinimalDto(null, null, null, null)));
    }

}
