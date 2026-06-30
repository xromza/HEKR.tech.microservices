package ru.xromza.catalog;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CatalogApplication}.
 */
@Generated
public class CatalogApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'catalogApplication'.
   */
  public static BeanDefinition getCatalogApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CatalogApplication.class);
    beanDefinition.setInstanceSupplier(CatalogApplication::new);
    return beanDefinition;
  }
}
