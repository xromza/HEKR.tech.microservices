package ru.xromza.catalog.mapper;

import ru.xromza.catalog.dto.CategoryResponseDto;
import ru.xromza.catalog.model.Category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryResponseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "count", ignore = true)
    CategoryResponseDto toDto(Category category);


    
    List<CategoryResponseDto> toResponseList(List<Category> categories);
}