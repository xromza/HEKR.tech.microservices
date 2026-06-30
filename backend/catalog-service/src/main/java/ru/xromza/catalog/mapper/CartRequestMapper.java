package ru.xromza.catalog.mapper;

import ru.xromza.catalog.dto.CartItemRequestDto;
import ru.xromza.catalog.model.Cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "quantity", source = "quantity")
    Cart toEntity(CartItemRequestDto dto);
}