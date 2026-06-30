package ru.xromza.catalog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.catalog.dto.ProductCatalogResponseDto;
import ru.xromza.catalog.model.Category;
import ru.xromza.catalog.model.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T01:44:46+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ProductCatalogResponseMapperImpl implements ProductCatalogResponseMapper {

    @Override
    public ProductCatalogResponseDto toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductCatalogResponseDto.ProductCatalogResponseDtoBuilder productCatalogResponseDto = ProductCatalogResponseDto.builder();

        productCatalogResponseDto.id( product.getId() );
        productCatalogResponseDto.title( product.getTitle() );
        productCatalogResponseDto.categoryId( productCategoryId( product ) );
        productCatalogResponseDto.categoryName( productCategoryName( product ) );
        productCatalogResponseDto.isActive( product.getIsActive() );
        productCatalogResponseDto.priceWholesale( product.getPriceWholesale() );
        productCatalogResponseDto.priceRetail( product.getPriceRetail() );
        productCatalogResponseDto.wholesaleThreshold( product.getWholesaleThreshold() );
        productCatalogResponseDto.brand( product.getBrand() );

        setMainVariantId( product, productCatalogResponseDto );
        setMainImageUrl( product, productCatalogResponseDto );

        return productCatalogResponseDto.build();
    }

    @Override
    public List<ProductCatalogResponseDto> toResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductCatalogResponseDto> list = new ArrayList<ProductCatalogResponseDto>( products.size() );
        for ( Product product : products ) {
            list.add( toResponse( product ) );
        }

        return list;
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
}
