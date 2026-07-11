package ru.xromza.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductVariantOrderDto {
    @Schema(description = "Уникальный идентификатор варианта продукта", example = "1")
    private Long variantId;
    @Schema(description = "Идентификатор продукта", example = "10")
    private Long productId;
    private String brand;
    private String title;
    @Schema(description = "Артикул варианта", example = "SKU-001")
    private String sku;
    @Schema(description = "Размер варианта", example = "M")
    private String size;
    @Schema(description = "Цвет варианта", example = "Черный")
    private String color;
    private String mainImageUrl;
}
