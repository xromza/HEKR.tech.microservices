package ru.xromza.catalog.service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.catalog.dto.ProductCatalogResponseDto;
import ru.xromza.catalog.dto.ProductRequestDto;
import ru.xromza.catalog.dto.ProductResponseDto;
import ru.xromza.catalog.exceptions.NotFoundException;
import ru.xromza.catalog.interfaces.ProductDtoInterface;
import ru.xromza.catalog.mapper.ProductCatalogResponseMapper;
import ru.xromza.catalog.mapper.ProductMapper;
import ru.xromza.catalog.model.Category;
import ru.xromza.catalog.model.Product;
import ru.xromza.catalog.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
        private final ProductRepository productRepository;
        private final ProductMapper productMapper;
        private final ProductCatalogResponseMapper productCatalogResponseMapper;
        private final CategoryService categoryService;

        @Transactional(readOnly = true)
        public Map<Long, Product> getProductsByVariantIds(List<Long> ids) {
                List<Product> products = productRepository.findAllByIds(ids);
                return products
                                .stream()
                                .collect(Collectors.toMap(product -> product.getId(), product -> product));
        }

        @Transactional(readOnly = true)
        public Page<? extends ProductDtoInterface> getProductCatalog(Pageable pageable, boolean verbose) {
                Page<Long> idsPage = productRepository.findProductIdsByIsActiveTrue(pageable);
                if (idsPage.isEmpty())
                        return Page.empty(pageable);
                List<Long> ids = idsPage.getContent();
                List<Product> products = productRepository.findAllByIds(ids);

                List<Product> sortedProducts = ids.stream()
                                .map(id -> products.stream()
                                                .filter(p -> p.getId().equals(id))
                                                .findFirst()
                                                .orElse(null))
                                .filter(Objects::nonNull)
                                .toList();
                if (verbose) {
                        return new PageImpl<ProductResponseDto>(productMapper.toResponseList(sortedProducts), pageable,
                                        idsPage.getTotalElements());
                }
                return new PageImpl<ProductCatalogResponseDto>(
                                productCatalogResponseMapper.toResponseList(sortedProducts),
                                pageable,
                                idsPage.getTotalElements());
        }

        public Page<? extends ProductDtoInterface> getProductCatalogByCategoryId(Pageable pageable, Long categoryId,
                        boolean verbose) {
                Page<Long> idsPage = productRepository.findProductIdsByIsActiveTrueAndCategoryId(pageable, categoryId);
                if (idsPage.isEmpty())
                        return Page.empty(pageable);
                List<Long> ids = idsPage.getContent();
                List<Product> products = productRepository.findAllByIds(ids);
                List<Product> sortedProducts = ids.stream()
                                .map(id -> products.stream()
                                                .filter(p -> p.getId().equals(id))
                                                .findFirst()
                                                .orElse(null))
                                .filter(Objects::nonNull)
                                .toList();
                if (verbose) {
                        return new PageImpl<ProductResponseDto>(productMapper.toResponseList(sortedProducts), pageable,
                                        idsPage.getTotalElements());
                }
                return new PageImpl<ProductCatalogResponseDto>(
                                productCatalogResponseMapper.toResponseList(sortedProducts), pageable,
                                idsPage.getTotalElements());
        }

        public Page<ProductDtoInterface> findProductsByTitle(String query, Pageable pageable) {

                return productRepository
                                .findByTitleContainingIgnoreCaseAndIsActiveTrue(query, pageable)
                                .map(productCatalogResponseMapper::toResponse);
        }

        public Page<ProductDtoInterface> findVerboseProductsByTitle(String query, Pageable pageable) {

                return productRepository
                                .findByTitleContainingIgnoreCaseAndIsActiveTrue(query, pageable)
                                .map(productMapper::toResponse);
        }

        public ProductResponseDto getProductDtoById(Long id) {
                Product product = productRepository
                                .findByIdWithVariantsAndImages(id)
                                .orElseThrow(
                                                () -> new NotFoundException("Товар с id " + id + " не найден"));
                return productMapper.toResponse(product);
        }

        public Product getProductById(Long id) {
                Product product = productRepository
                                .findByIdWithVariantsAndImages(id)
                                .orElseThrow(
                                                () -> new NotFoundException("Товар с id " + id + " не найден"));
                return product;
        }

        @Transactional
        public ProductResponseDto createProduct(ProductRequestDto dto) {
                Category category = categoryService.getCategoryById(dto.getCategoryId());
                Product product = Product.builder()
                                .brand(dto.getBrand())
                                .category(category)
                                .description(dto.getDescription())
                                .isActive(true)
                                .priceRetail(dto.getPriceRetail())
                                .priceWholesale(dto.getPriceWholesale())
                                .title(dto.getTitle())
                                .variants(new ArrayList<>())
                                .wholesaleThreshold(dto.getWholesaleThreshold())
                                .build();
                return productMapper.toResponse(productRepository.save(product));
        }

        @Transactional(readOnly = true)
        public Long countByCategoryId(Long categoryId) {
                return productRepository.countProductsByCategoryId(categoryId);
        }
}
