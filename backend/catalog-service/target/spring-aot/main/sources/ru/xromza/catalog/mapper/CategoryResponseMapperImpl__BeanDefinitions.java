package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CategoryResponseMapperImpl}.
 */
@Generated
public class CategoryResponseMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'categoryResponseMapperImpl'.
   */
  public static BeanDefinition getCategoryResponseMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CategoryResponseMapperImpl.class);
    beanDefinition.setInstanceSupplier(CategoryResponseMapperImpl::new);
    return beanDefinition;
  }
}
