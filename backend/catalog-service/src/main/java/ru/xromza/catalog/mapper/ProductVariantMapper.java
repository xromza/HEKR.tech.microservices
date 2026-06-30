package ru.xromza.catalog.mapper;

import ru.xromza.catalog.dto.ProductVariantResponseDto;
import ru.xromza.catalog.model.ProductVariant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface ProductVariantMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "images", source = "images")
    ProductVariantResponseDto toDto(ProductVariant variant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "size", source = "size")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "isActive", source = "isActive")
    ProductVariant toEntity(ProductVariantResponseDto dto);

    List<ProductVariant> toEntityList(List<ProductVariantResponseDto> dtos);
    List<ProductVariantResponseDto> toDtoList(List<ProductVariant> variants);
}