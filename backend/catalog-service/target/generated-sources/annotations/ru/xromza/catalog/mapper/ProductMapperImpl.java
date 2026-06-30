package ru.xromza.catalog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xromza.catalog.dto.ProductResponseDto;
import ru.xromza.catalog.model.Category;
import ru.xromza.catalog.model.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T01:44:46+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Override
    public ProductResponseDto toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponseDto.ProductResponseDtoBuilder productResponseDto = ProductResponseDto.builder();

        productResponseDto.categoryName( productCategoryName( product ) );
        productResponseDto.variants( productVariantMapper.toDtoList( product.getVariants() ) );
        productResponseDto.categoryId( productCategoryId( product ) );
        productResponseDto.brand( product.getBrand() );
        productResponseDto.description( product.getDescription() );
        productResponseDto.id( product.getId() );
        productResponseDto.isActive( product.getIsActive() );
        productResponseDto.priceRetail( product.getPriceRetail() );
        productResponseDto.priceWholesale( product.getPriceWholesale() );
        productResponseDto.title( product.getTitle() );
        productResponseDto.wholesaleThreshold( product.getWholesaleThreshold() );

        setMainImageUrl( product, productResponseDto );

        return productResponseDto.build();
    }

    @Override
    public List<ProductResponseDto> toResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponseDto> list = new ArrayList<ProductResponseDto>( products.size() );
        for ( Product product : products ) {
            list.add( toResponse( product ) );
        }

        return list;
    }

    private String productCategoryName(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long productCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
