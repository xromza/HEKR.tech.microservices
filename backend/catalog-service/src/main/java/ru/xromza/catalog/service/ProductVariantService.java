package ru.xromza.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.catalog.dto.ProductMinimalDto;
import ru.xromza.catalog.dto.ProductVariantOrderDto;
import ru.xromza.catalog.dto.ProductVariantRequestDto;
import ru.xromza.catalog.dto.ProductVariantResponseDto;
import ru.xromza.catalog.exceptions.NotFoundException;
import ru.xromza.catalog.interfaces.ProductDtoInterface;
import ru.xromza.catalog.mapper.ProductMapper;
import ru.xromza.catalog.mapper.ProductVariantMapper;
import ru.xromza.catalog.model.Image;
import ru.xromza.catalog.model.Product;
import ru.xromza.catalog.model.ProductVariant;
import ru.xromza.catalog.repository.ProductVariantsRepository;
import ru.xromza.catalog.utils.ImageType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductService productService;
    private final ProductVariantMapper productVariantMapper;
    private final ProductVariantsRepository productVariantsRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductVariantResponseDto createVariant(ProductVariantRequestDto dto, Long productId) {
        Product product = productService.getProductById(productId);

        ProductVariant productVariant = ProductVariant.builder()
                .color(dto.getColor())
                .images(Set.of())
                .sku(dto.getSku())
                .size(dto.getSize())
                .weight(dto.getWeight())
                .product(product)
                .isActive(true)
                .build();
        return productVariantMapper.toDto(productVariantsRepository.save(productVariant));
    }

    @Transactional(readOnly = true)
    public ProductVariant getProductVariantById(Long id) {
        return productVariantsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вариант товара не найден"));
    }

    @Transactional(readOnly = true)
    public ProductDtoInterface findProductByVariantId(Long variantId) {
        return productMapper.toResponse(
                getProductVariantById(variantId).getProduct());
    }

    @Transactional(readOnly = true)
    public Map<Long, ProductVariant> getAllVariantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        List<ProductVariant> variants = productVariantsRepository.findAllVariantsByIds(ids);
        return variants.stream()
                .collect(Collectors.toMap(variant -> variant.getId(), variant -> variant));

    }

    @Transactional(readOnly = true)
    public Map<Long, ProductVariantOrderDto> getAllVariantsOrderByIds(List<Long> ids) {
        List<ProductVariant> variants = productVariantsRepository.findAllVariantsByIds(ids);
        List<Long> productIds = variants.stream().map(variant -> variant.getProduct().getId()).toList();
        Map<Long, Product> products = productService.getProductsByIds(productIds);
        Map<Long, ProductVariantOrderDto> productVariantOrderDtos = variants.stream().map(item -> {
            Product product = products.get(item.getProduct().getId());
            String mainImageUrl = product.getVariants().stream()
                    .filter(v -> v.getImages() != null)
                    .flatMap(v -> v.getImages().stream())
                    .filter(img -> img.getType() == ImageType.THUMBNAIL)
                    .map(Image::getUrl)
                    .findFirst()
                    .or(() -> product.getVariants().stream()
                            .filter(v -> v.getImages() != null)
                            .flatMap(v -> v.getImages().stream())
                            .filter(img -> img.getType() == ImageType.MAIN)
                            .map(Image::getUrl)
                            .findFirst())
                    .or(() -> product.getVariants().stream()
                            .filter(v -> v.getImages() != null)
                            .flatMap(v -> v.getImages().stream())
                            .map(Image::getUrl)
                            .findFirst())
                    .orElse(null);
            return ProductVariantOrderDto.builder()
                    .brand(product.getBrand())
                    .color(item.getColor())
                    .size(item.getSize())
                    .sku(item.getSku())
                    .title(product.getTitle())
                    .variantId(item.getId())
                    .productId(product.getId())
                    .mainImageUrl(mainImageUrl)
                    .build();
        }).collect(Collectors.toMap(item -> item.getVariantId(), item -> item));
        return productVariantOrderDtos;
    }

    @Transactional(readOnly = true)
    public Map<Long, ProductMinimalDto> getItemPrices(List<Long> variantIds) {
        Map<Long, ProductVariant> variants = getAllVariantsByIds(variantIds);
        List<Long> productIds = variants.entrySet().stream()
                .map(item -> item.getValue().getProduct().getId())
                .toList();
        Map<Long, ProductMinimalDto> products = productService.getProductsMinimalByIds(productIds);
        return products;
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long variantId) {
        return productVariantsRepository.existsById(variantId);
    }

}
