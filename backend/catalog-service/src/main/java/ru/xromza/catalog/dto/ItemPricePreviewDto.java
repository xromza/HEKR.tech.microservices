package ru.xromza.catalog.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPricePreviewDto {
    private BigDecimal priceRetail;
    private BigDecimal priceWholesale;
    private Integer wholesaleThreshold;
}
