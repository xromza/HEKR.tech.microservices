package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ImageMapperImpl}.
 */
@Generated
public class ImageMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'imageMapperImpl'.
   */
  public static BeanDefinition getImageMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ImageMapperImpl.class);
    beanDefinition.setInstanceSupplier(ImageMapperImpl::new);
    return beanDefinition;
  }
}
