package ru.xromza.warehouse.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemDto {
    private Long variantId;
    private Integer quantity; 
}
