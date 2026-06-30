package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CategoryRequestMapperImpl}.
 */
@Generated
public class CategoryRequestMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'categoryRequestMapperImpl'.
   */
  public static BeanDefinition getCategoryRequestMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CategoryRequestMapperImpl.class);
    beanDefinition.setInstanceSupplier(CategoryRequestMapperImpl::new);
    return beanDefinition;
  }
}
