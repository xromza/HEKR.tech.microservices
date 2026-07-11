package ru.xromza.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class OrderItem {
    @Schema(description = "Идентификатор варианта товара", example = "1")
    @NotNull
    private Long variantId;
    @Schema(description = "Количество товара", example = "2")
    @Min(1)
    @NotNull
    private Integer quantity;
}
