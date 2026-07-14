package ru.xromza.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemWarehouseAvailabilityResponseDto {
    public Long warehouseId;
    public Integer availableQuantity;
}
