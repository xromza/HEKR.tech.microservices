package ru.xromza.order.dto;

import java.math.BigDecimal;

public record ProductMinimalDto(
        Long id,
        BigDecimal priceRetail,
        BigDecimal priceWholesale,
        Integer wholesaleThreshold) {
}
