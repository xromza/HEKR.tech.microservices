package ru.xromza.order_worker.dto;

import java.math.BigDecimal;

public record ProductMinimalDto(
        Long id,
        BigDecimal priceRetail,
        BigDecimal priceWholesale,
        Integer wholesaleThreshold) {
}
