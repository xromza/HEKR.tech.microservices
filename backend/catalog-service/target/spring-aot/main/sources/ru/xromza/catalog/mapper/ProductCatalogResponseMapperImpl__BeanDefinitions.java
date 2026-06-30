package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductCatalogResponseMapperImpl}.
 */
@Generated
public class ProductCatalogResponseMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'productCatalogResponseMapperImpl'.
   */
  public static BeanDefinition getProductCatalogResponseMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductCatalogResponseMapperImpl.class);
    beanDefinition.setInstanceSupplier(ProductCatalogResponseMapperImpl::new);
    return beanDefinition;
  }
}
