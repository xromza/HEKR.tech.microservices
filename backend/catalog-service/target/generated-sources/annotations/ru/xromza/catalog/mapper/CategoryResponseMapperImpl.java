package ru.xromza.catalog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.catalog.dto.CategoryResponseDto;
import ru.xromza.catalog.model.Category;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T01:44:46+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CategoryResponseMapperImpl implements CategoryResponseMapper {

    @Override
    public CategoryResponseDto toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponseDto.CategoryResponseDtoBuilder categoryResponseDto = CategoryResponseDto.builder();

        categoryResponseDto.id( category.getId() );
        categoryResponseDto.name( category.getName() );

        return categoryResponseDto.build();
    }

    @Override
    public List<CategoryResponseDto> toResponseList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponseDto> list = new ArrayList<CategoryResponseDto>( categories.size() );
        for ( Category category : categories ) {
            list.add( toDto( category ) );
        }

        return list;
    }
}
