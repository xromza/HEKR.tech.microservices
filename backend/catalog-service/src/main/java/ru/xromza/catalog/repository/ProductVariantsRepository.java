package ru.xromza.catalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.xromza.catalog.model.ProductVariant;


public interface ProductVariantsRepository extends JpaRepository<ProductVariant, Long> {
    @Query("SELECT DISTINCT pv FROM ProductVariant pv LEFT JOIN FETCH pv.images WHERE pv.id = :id AND pv.product.id = :productId")
    Optional<ProductVariant> findByIdWithImages(@Param("id") Long id, @Param("productId") Long productId);

    @Query("SELECT DISTINCT pv FROM ProductVariant pv LEFT JOIN FETCH pv.images WHERE pv.product.id = :productId")
    List<ProductVariant> findByProductIdWithImages(Long productId);
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.id IN :ids")
    List<ProductVariant> findAllVariantsByIds(List<Long> ids);
}
