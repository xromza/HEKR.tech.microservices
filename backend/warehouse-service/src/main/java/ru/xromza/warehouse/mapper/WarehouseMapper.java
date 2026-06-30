package ru.xromza.warehouse.mapper;

import ru.xromza.warehouse.dto.PreOrderWarehouseResponseDto;
import ru.xromza.warehouse.dto.WarehouseRequestDto;
import ru.xromza.warehouse.dto.WarehouseResponseDto;
import ru.xromza.warehouse.model.Warehouse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "address")
    Warehouse toEntity(WarehouseRequestDto requestDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    WarehouseResponseDto toResponse(Warehouse warehouse);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "isAvailableForOrder", ignore = true)
    PreOrderWarehouseResponseDto toPreOrderResponse(Warehouse warehouse);

    List<WarehouseResponseDto> toResponseList(List<Warehouse> warehouses);
}