package ru.xromza.catalog.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;
import org.springframework.aot.generate.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.DeclaredQuery;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;
import org.springframework.data.support.PageableExecutionUtils;
import ru.xromza.catalog.model.Product;

/**
 * AOT generated JPA repository implementation for {@link ProductRepository}.
 */
@Generated
public class ProductRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ProductRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ProductRepository#countProductsByCategoryId(java.lang.Long)}.
   */
  public Long countProductsByCategoryId(@Param("id") Long id) {
    String queryString = "SELECT COUNT(p) FROM Product p WHERE p.category.id = :id";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("id", id);

    return (Long) convertOne(query.getSingleResultOrNull(), false, Long.class);
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findAllActiveByIdCategoryId(java.lang.Long,org.springframework.data.domain.Pageable)}.
   */
  public Page<Product> findAllActiveByIdCategoryId(@Param("categoryId") Long categoryId,
      Pageable pageable) {
    String queryString = "SELECT p FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId";
    String countQueryString = "SELECT count(p) FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId";
    Pageable pageable_1 = pageable != null ? pageable : Pageable.unpaged();
    if (pageable_1.getSort().isSorted()) {
      DeclaredQuery declaredQuery = DeclaredQuery.jpqlQuery(queryString);
      queryString = rewriteQuery(declaredQuery, pageable_1.getSort(), Product.class);
    }
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("categoryId", categoryId);
    if (pageable_1.isPaged()) {
      query.setFirstResult(Long.valueOf(pageable_1.getOffset()).intValue());
      query.setMaxResults(pageable_1.getPageSize());
    }
    LongSupplier countAll = () -> {
      Query countQuery = this.entityManager.createQuery(countQueryString);
      countQuery.setParameter("categoryId", categoryId);
      return getCount(countQuery);
    };

    return PageableExecutionUtils.getPage((List<Product>) query.getResultList(), pageable_1, countAll);
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findAllActiveByIdCategoryIdVerbose(java.lang.Long,org.springframework.data.domain.Pageable)}.
   */
  public Page<Product> findAllActiveByIdCategoryIdVerbose(@Param("categoryId") Long categoryId,
      Pageable pageable) {
    String queryString = "SELECT p FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId";
    String countQueryString = "SELECT count(p) FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId";
    Pageable pageable_1 = pageable != null ? pageable : Pageable.unpaged();
    if (pageable_1.getSort().isSorted()) {
      DeclaredQuery declaredQuery = DeclaredQuery.jpqlQuery(queryString);
      queryString = rewriteQuery(declaredQuery, pageable_1.getSort(), Product.class);
    }
    Query query = this.entityManager.createQuery(queryString);
    EntityGraph<Product> entityGraph = entityManager.createEntityGraph(Product.class);
    entityGraph.addAttributeNodes("variants");
    query.setHint("jakarta.persistence.fetchgraph", entityGraph);
    query.setParameter("categoryId", categoryId);
    if (pageable_1.isPaged()) {
      query.setFirstResult(Long.valueOf(pageable_1.getOffset()).intValue());
      query.setMaxResults(pageable_1.getPageSize());
    }
    LongSupplier countAll = () -> {
      Query countQuery = this.entityManager.createQuery(countQueryString);
      countQuery.setParameter("categoryId", categoryId);
      return getCount(countQuery);
    };

    return PageableExecutionUtils.getPage((List<Product>) query.getResultList(), pageable_1, countAll);
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findAllActiveWithVariants(org.springframework.data.domain.Pageable)}.
   */
  public Page<Product> findAllActiveWithVariants(Pageable pageable) {
    String queryString = "SELECT DISTINCT p FROM Product p LEFT JOIN p.variants WHERE p.isActive = true";
    String countQueryString = "SELECT count(DISTINCT p) FROM Product p LEFT JOIN p.variants WHERE p.isActive = true";
    Pageable pageable_1 = pageable != null ? pageable : Pageable.unpaged();
    if (pageable_1.getSort().isSorted()) {
      DeclaredQuery declaredQuery = DeclaredQuery.jpqlQuery(queryString);
      queryString = rewriteQuery(declaredQuery, pageable_1.getSort(), Product.class);
    }
    Query query = this.entityManager.createQuery(queryString);
    if (pageable_1.isPaged()) {
      query.setFirstResult(Long.valueOf(pageable_1.getOffset()).intValue());
      query.setMaxResults(pageable_1.getPageSize());
    }
    LongSupplier countAll = () -> {
      Query countQuery = this.entityManager.createQuery(countQueryString);
      return getCount(countQuery);
    };

    return PageableExecutionUtils.getPage((List<Product>) query.getResultList(), pageable_1, countAll);
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findAllByIds(java.util.List)}.
   */
  public List<Product> findAllByIds(List<Long> ids) {
    String queryString = "SELECT DISCTINT p FROM Product p JOIN FETCH p.category LEFT JOIN FETCH p.variants v LEFT JOIN FETCH v.images WHERE p.id IN :ids";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("ids", ids);

    return (List<Product>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findAllMinimalByIsActiveTrue(org.springframework.data.domain.Pageable)}.
   */
  public List<Product> findAllMinimalByIsActiveTrue(Pageable pageable) {
    String queryString = "SELECT p FROM Product p WHERE p.isActive = TRUE";
    Pageable pageable_1 = pageable != null ? pageable : Pageable.unpaged();
    if (pageable_1.getSort().isSorted()) {
      DeclaredQuery declaredQuery = DeclaredQuery.jpqlQuery(queryString);
      queryString = rewriteQuery(declaredQuery, pageable_1.getSort(), Product.class);
    }
    Query query = this.entityManager.createQuery(queryString);
    if (pageable_1.isPaged()) {
      query.setFirstResult(Long.valueOf(pageable_1.getOffset()).intValue());
      query.setMaxResults(pageable_1.getPageSize());
    }

    return (List<Product>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findByIdWithVariants(java.lang.Long)}.
   */
  public Optional<Product> findByIdWithVariants(Long id) {
    String queryString = "SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :id";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("id", id);

    return Optional.ofNullable((Product) convertOne(query.getSingleResultOrNull(), false, Product.class));
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findByIdWithVariantsAndImages(java.lang.Long)}.
   */
  public Optional<Product> findByIdWithVariantsAndImages(@Param("id") Long id) {
    String queryString = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.variants v LEFT JOIN FETCH v.images WHERE p.id = :id";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("id", id);

    return Optional.ofNullable((Product) convertOne(query.getSingleResultOrNull(), false, Product.class));
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findByTitleContainingIgnoreCase(java.lang.String,org.springframework.data.domain.Pageable)}.
   */
  public Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable) {
    String queryString = "SELECT p FROM Product p WHERE UPPER(p.title) LIKE UPPER(:title) ESCAPE '\\'";
    String countQueryString = "SELECT COUNT(p) FROM Product p WHERE UPPER(p.title) LIKE UPPER(:title) ESCAPE '\\'";
    Pageable pageable_1 = pageable != null ? pageable : Pageable.unpaged();
    if (pageable_1.getSort().isSorted()) {
      DeclaredQuery declaredQuery = DeclaredQuery.jpqlQuery(queryString);
      queryString = rewriteQuery(declaredQuery, pageable_1.getSort(), Product.class);
    }
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("title", "%%%s%%".formatted(title != null ? title.toUpperCase() : title));
    if (pageable_1.isPaged()) {
      query.setFirstResult(Long.valueOf(pageable_1.getOffset()).intValue());
      query.setMaxResults(pageable_1.getPageSize());
    }
    LongSupplier countAll = () -> {
      Query countQuery = this.entityManager.createQuery(countQueryString);
      countQuery.setParameter("title", "%%%s%%".formatted(title != null ? title.toUpperCase() : title));
      return getCount(countQuery);
    };

    return PageableExecutionUtils.getPage((List<Product>) query.getResultList(), pageable_1, countAll);
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findByTitleContainingIgnoreCaseAndIsActiveTrue(java.lang.String,org.springframework.data.domain.Pageable)}.
   */
  public Page<Product> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title,
      Pageable pageable) {
    String queryString = "SELECT p FROM Product p WHERE UPPER(p.title) LIKE UPPER(:title) ESCAPE '\\' AND p.isActive = TRUE";
    String countQueryString = "SELECT COUNT(p) FROM Product p WHERE UPPER(p.title) LIKE UPPER(:title) ESCAPE '\\' AND p.isActive = TRUE";
    Pageable pageable_1 = pageable != null ? pageable : Pageable.unpaged();
    if (pageable_1.getSort().isSorted()) {
      DeclaredQuery declaredQuery = DeclaredQuery.jpqlQuery(queryString);
      queryString = rewriteQuery(declaredQuery, pageable_1.getSort(), Product.class);
    }
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("title", "%%%s%%".formatted(title != null ? title.toUpperCase() : title));
    if (pageable_1.isPaged()) {
      query.setFirstResult(Long.valueOf(pageable_1.getOffset()).intValue());
      query.setMaxResults(pageable_1.getPageSize());
    }
    LongSupplier countAll = () -> {
      Query countQuery = this.entityManager.createQuery(countQueryString);
      countQuery.setParameter("title", "%%%s%%".formatted(title != null ? title.toUpperCase() : title));
      return getCount(countQuery);
    };

    return PageableExecutionUtils.getPage((List<Product>) query.getResultList(), pageable_1, countAll);
  }
}
