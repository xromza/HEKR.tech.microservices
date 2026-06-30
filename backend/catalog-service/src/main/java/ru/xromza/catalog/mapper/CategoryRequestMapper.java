package ru.xromza.catalog.mapper;

import ru.xromza.catalog.dto.CategoryRequestDto;
import ru.xromza.catalog.model.Category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    Category toEntity(CategoryRequestDto dto);

    List<Category> toEntityList(List<CategoryRequestDto> dtos);
}