package ru.xromza.catalog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.xromza.catalog.dto.ProductMinimalDto;
import ru.xromza.catalog.dto.ProductResponseDto;
import ru.xromza.catalog.dto.ProductVariantOrderDto;
import ru.xromza.catalog.interfaces.ProductDtoInterface;
import ru.xromza.catalog.service.ProductService;
import ru.xromza.catalog.service.ProductVariantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Каталог товаров", description = "Работа с витриной и поиском")
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProductVariantService productVariantService;

    @GetMapping
    public Page<? extends ProductDtoInterface> getProducts(
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") boolean verbose) {
        return productService.getProductCatalog(pageable, verbose);
    }

    @GetMapping("/items")
    public ResponseEntity<Map<Long, ProductVariantOrderDto>> getVariantOrderView(@RequestParam List<Long> variantIds) {
        return ResponseEntity.ok(productVariantService.getAllVariantsOrderByIds(variantIds));
    }

    @GetMapping("/category/{id}")
    public Page<? extends ProductDtoInterface> getProductsOfCategory(
            Pageable pageable,
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean verbose) {
        return productService.getProductCatalogByCategoryId(pageable, id, verbose);
    }

    @GetMapping("/search")
    public Page<ProductDtoInterface> searchProductsByTitle(
            @RequestParam(required = true, defaultValue = "") String query,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") Boolean verbose) {
        log.info("Поступил запрос поиска на query = {}", query);
        if (verbose) {
            return productService.findVerboseProductsByTitle(query, pageable);
        }
        return productService.findProductsByTitle(query, pageable);
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<Long, ProductMinimalDto>> getItemPrices(@RequestParam List<Long> variantIds) {
        return ResponseEntity.ok(productVariantService.getItemPrices(variantIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDtoById(id));
    }

}
