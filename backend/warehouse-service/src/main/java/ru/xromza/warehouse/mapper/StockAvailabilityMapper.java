package ru.xromza.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.xromza.warehouse.dto.ItemWarehouseAvailabilityResponseDto;
import ru.xromza.warehouse.model.Stock;

@Mapper(componentModel = "spring")
public interface StockAvailabilityMapper {

    @Mapping(target="warehouseId", source = "warehouse.id")
    @Mapping(target="availableQuantity", source = "quantity")
    ItemWarehouseAvailabilityResponseDto toResponse(Stock stock);
}
