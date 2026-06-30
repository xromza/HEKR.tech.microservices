package ru.xromza.catalog.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.util.Collection;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import ru.xromza.catalog.model.Category;

/**
 * AOT generated JPA repository implementation for {@link CategoryRepository}.
 */
@Generated
public class CategoryRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public CategoryRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link CategoryRepository#existsByName(java.lang.String)}.
   */
  public Boolean existsByName(String name) {
    String queryString = "SELECT c.id FROM Category c WHERE c.name = :name";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("name", name);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link CategoryRepository#findAllByIdIn(java.util.Collection)}.
   */
  public List<Category> findAllByIdIn(Collection<Long> ids) {
    String queryString = "SELECT c FROM Category c WHERE c.id IN :ids";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("ids", ids);

    return (List<Category>) query.getResultList();
  }
}
