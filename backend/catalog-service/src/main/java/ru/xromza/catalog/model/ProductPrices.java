package ru.xromza.catalog.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductPrices {
    private BigDecimal priceRetail;
    private BigDecimal priceWholesale;
    private Integer wholesaleThreshold;
}
