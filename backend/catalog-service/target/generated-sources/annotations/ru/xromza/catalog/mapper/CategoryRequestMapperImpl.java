package ru.xromza.catalog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.catalog.dto.CategoryRequestDto;
import ru.xromza.catalog.model.Category;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T01:44:46+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CategoryRequestMapperImpl implements CategoryRequestMapper {

    @Override
    public Category toEntity(CategoryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( dto.getName() );

        return category.build();
    }

    @Override
    public List<Category> toEntityList(List<CategoryRequestDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Category> list = new ArrayList<Category>( dtos.size() );
        for ( CategoryRequestDto categoryRequestDto : dtos ) {
            list.add( toEntity( categoryRequestDto ) );
        }

        return list;
    }
}
