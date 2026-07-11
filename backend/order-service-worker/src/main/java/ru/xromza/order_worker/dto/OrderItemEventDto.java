package ru.xromza.order_worker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderItemEventDto {
    private Long variantId;
    private Integer quantity;
}
