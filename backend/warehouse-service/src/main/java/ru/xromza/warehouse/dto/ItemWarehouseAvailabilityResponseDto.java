package ru.xromza.warehouse.dto;

import lombok.Getter;

@Getter
public class ItemWarehouseAvailabilityResponseDto {
    public Long warehouseId;
    public Integer availableQuantity;
}
