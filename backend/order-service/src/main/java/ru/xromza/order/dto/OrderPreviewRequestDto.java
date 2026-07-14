package ru.xromza.order.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPreviewRequestDto {
    private List<OrderItemRequestDto> items;
    private Long warehouseId;
}
