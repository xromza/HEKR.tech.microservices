package ru.xromza.warehouse.mapper;

import ru.xromza.warehouse.dto.StockResponsePlainDto;
import ru.xromza.warehouse.model.Stock;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StockResponsePlainMapper {
    @Mapping(target = "variantId", source = "id.variantId")
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "size", ignore = true)
    StockResponsePlainDto toResponse(Stock stock);

    List<StockResponsePlainDto> toResponseList(Collection<Stock> stock);
}