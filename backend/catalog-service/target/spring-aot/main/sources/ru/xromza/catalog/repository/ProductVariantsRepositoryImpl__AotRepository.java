package ru.xromza.catalog.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;
import ru.xromza.catalog.model.ProductVariant;

/**
 * AOT generated JPA repository implementation for {@link ProductVariantsRepository}.
 */
@Generated
public class ProductVariantsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ProductVariantsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ProductVariantsRepository#findAllVariantsByIds(java.util.List)}.
   */
  public List<ProductVariant> findAllVariantsByIds(List<Long> ids) {
    String queryString = "SELECT pv FROM ProductVariant pv WHERE pv.id IN :ids";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("ids", ids);

    return (List<ProductVariant>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ProductVariantsRepository#findByIdWithImages(java.lang.Long,java.lang.Long)}.
   */
  public Optional<ProductVariant> findByIdWithImages(@Param("id") Long id,
      @Param("productId") Long productId) {
    String queryString = "SELECT DISTINCT pv FROM ProductVariant pv LEFT JOIN FETCH pv.images WHERE pv.id = :id AND pv.product.id = :productId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("id", id);
    query.setParameter("productId", productId);

    return Optional.ofNullable((ProductVariant) convertOne(query.getSingleResultOrNull(), false, ProductVariant.class));
  }

  /**
   * AOT generated implementation of {@link ProductVariantsRepository#findByProductIdWithImages(java.lang.Long)}.
   */
  public List<ProductVariant> findByProductIdWithImages(Long productId) {
    String queryString = "SELECT DISTINCT pv FROM ProductVariant pv LEFT JOIN FETCH pv.images WHERE pv.product.id = :productId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("productId", productId);

    return (List<ProductVariant>) query.getResultList();
  }
}
