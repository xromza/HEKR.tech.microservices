package ru.xromza.catalog.dto;

import java.math.BigDecimal;

public record ProductMinimalDto(
        Long id,
        BigDecimal priceRetail,
        BigDecimal priceWholesale,
        Integer wholesaleThreshold) {
}
