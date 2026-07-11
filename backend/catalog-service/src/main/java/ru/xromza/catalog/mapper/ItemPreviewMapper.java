package ru.xromza.catalog.mapper;

import org.mapstruct.Mapper;

import ru.xromza.catalog.dto.ItemPricePreviewDto;
import ru.xromza.catalog.model.Product;

@Mapper(componentModel = "spring")
public interface ItemPreviewMapper {
    public ItemPricePreviewDto toDto(Product product);
}
