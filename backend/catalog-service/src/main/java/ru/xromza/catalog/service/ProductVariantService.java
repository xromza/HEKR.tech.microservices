package ru.xromza.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.catalog.dto.ProductVariantRequestDto;
import ru.xromza.catalog.dto.ProductVariantResponseDto;
import ru.xromza.catalog.exceptions.NotFoundException;
import ru.xromza.catalog.interfaces.ProductDtoInterface;
import ru.xromza.catalog.mapper.ProductMapper;
import ru.xromza.catalog.mapper.ProductVariantMapper;
import ru.xromza.catalog.model.Product;
import ru.xromza.catalog.model.ProductVariant;
import ru.xromza.catalog.repository.ProductVariantsRepository;

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

    public ProductDtoInterface findProductByVariantId(Long variantId) {
        return productMapper.toResponse(
                getProductVariantById(variantId).getProduct());
    }

    @Transactional
    public Map<Long, ProductVariant> getAllVariantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        List<ProductVariant> variants = productVariantsRepository.findAllVariantsByIds(ids);
        return variants.stream()
                .collect(Collectors.toMap(variant -> variant.getId(), variant -> variant));

    }

}
