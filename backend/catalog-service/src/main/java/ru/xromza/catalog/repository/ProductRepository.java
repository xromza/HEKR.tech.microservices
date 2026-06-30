package ru.xromza.catalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.xromza.catalog.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.variants v LEFT JOIN FETCH v.images WHERE p.id = :id")
    Optional<Product> findByIdWithVariantsAndImages(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :id")
    Optional<Product> findByIdWithVariants(Long id);

    @Query("SELECT p.id FROM Product p WHERE p.isActive = true")
    Page<Long> findProductIdsByIsActiveTrue(Pageable pageable);

    @Query("SELECT p.id FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId")
    Page<Long> findProductIdsByIsActiveTrueAndCategoryId(Pageable pageable, Long categoryId);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category LEFT JOIN FETCH p.variants v LEFT JOIN FETCH v.images WHERE p.id IN :ids")
    List<Product> findAllByIds(List<Long> ids);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.variants WHERE p.isActive = true")
    Page<Product> findAllActiveWithVariants(Pageable pageable);

    @EntityGraph(attributePaths = { "variants" })
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId")
    Page<Product> findAllActiveByIdCategoryIdVerbose(@Param("categoryId") Long categoryId, Pageable pageable);

    Page<Product> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title, Pageable pageable);

    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :id")
    Long countProductsByCategoryId(@Param("id") Long id);
}
