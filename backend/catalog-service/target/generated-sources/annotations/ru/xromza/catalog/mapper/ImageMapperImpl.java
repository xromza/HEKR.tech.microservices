package ru.xromza.catalog.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.catalog.dto.ImagesDto;
import ru.xromza.catalog.model.Image;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T01:44:46+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ImageMapperImpl implements ImageMapper {

    @Override
    public ImagesDto toDto(Image image) {
        if ( image == null ) {
            return null;
        }

        ImagesDto.ImagesDtoBuilder imagesDto = ImagesDto.builder();

        imagesDto.id( image.getId() );
        imagesDto.url( image.getUrl() );
        imagesDto.type( image.getType() );
        imagesDto.sortOrder( image.getSortOrder() );
        imagesDto.createdAt( image.getCreatedAt() );

        return imagesDto.build();
    }

    @Override
    public List<ImagesDto> toDtoList(Set<Image> images) {
        if ( images == null ) {
            return null;
        }

        List<ImagesDto> list = new ArrayList<ImagesDto>( images.size() );
        for ( Image image : images ) {
            list.add( toDto( image ) );
        }

        return list;
    }

    @Override
    public Image toEntity(ImagesDto dto) {
        if ( dto == null ) {
            return null;
        }

        Image.ImageBuilder image = Image.builder();

        image.url( dto.getUrl() );
        image.type( dto.getType() );
        image.sortOrder( dto.getSortOrder() );

        return image.build();
    }
}
