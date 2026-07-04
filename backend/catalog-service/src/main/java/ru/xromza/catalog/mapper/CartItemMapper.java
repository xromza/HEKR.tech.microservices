package ru.xromza.catalog.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.xromza.catalog.dto.CartItemRequestDto;
import ru.xromza.catalog.dto.CartItemResponseDto;
import ru.xromza.catalog.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "variantId", source = "variantId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "appliedPrice", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "priceType", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    public CartItemResponseDto toDto(CartItem cartItem);


    @Mapping(target = "variantId", source = "variantId")
    @Mapping(target = "quantity", source = "quantity")
    public CartItem toEntity(CartItemRequestDto dto);


    public List<CartItemResponseDto> toListDto(List<CartItem> cartItems);
}
