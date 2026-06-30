package ru.xromza.catalog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xromza.catalog.dto.ProductVariantResponseDto;
import ru.xromza.catalog.model.Product;
import ru.xromza.catalog.model.ProductVariant;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T01:44:46+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ProductVariantMapperImpl implements ProductVariantMapper {

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public ProductVariantResponseDto toDto(ProductVariant variant) {
        if ( variant == null ) {
            return null;
        }

        ProductVariantResponseDto.ProductVariantResponseDtoBuilder productVariantResponseDto = ProductVariantResponseDto.builder();

        productVariantResponseDto.id( variant.getId() );
        productVariantResponseDto.productId( variantProductId( variant ) );
        productVariantResponseDto.sku( variant.getSku() );
        productVariantResponseDto.size( variant.getSize() );
        productVariantResponseDto.color( variant.getColor() );
        productVariantResponseDto.isActive( variant.getIsActive() );
        productVariantResponseDto.images( imageMapper.toDtoList( variant.getImages() ) );
        productVariantResponseDto.weight( variant.getWeight() );

        return productVariantResponseDto.build();
    }

    @Override
    public ProductVariant toEntity(ProductVariantResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProductVariant.ProductVariantBuilder productVariant = ProductVariant.builder();

        productVariant.size( dto.getSize() );
        productVariant.color( dto.getColor() );
        productVariant.weight( dto.getWeight() );
        productVariant.sku( dto.getSku() );
        productVariant.isActive( dto.getIsActive() );

        return productVariant.build();
    }

    @Override
    public List<ProductVariant> toEntityList(List<ProductVariantResponseDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<ProductVariant> list = new ArrayList<ProductVariant>( dtos.size() );
        for ( ProductVariantResponseDto productVariantResponseDto : dtos ) {
            list.add( toEntity( productVariantResponseDto ) );
        }

        return list;
    }

    @Override
    public List<ProductVariantResponseDto> toDtoList(List<ProductVariant> variants) {
        if ( variants == null ) {
            return null;
        }

        List<ProductVariantResponseDto> list = new ArrayList<ProductVariantResponseDto>( variants.size() );
        for ( ProductVariant productVariant : variants ) {
            list.add( toDto( productVariant ) );
        }

        return list;
    }

    private Long variantProductId(ProductVariant productVariant) {
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
