package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductMapperImpl}.
 */
@Generated
public class ProductMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'productMapperImpl'.
   */
  public static BeanDefinition getProductMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductMapperImpl.class);
    InstanceSupplier<ProductMapperImpl> instanceSupplier = InstanceSupplier.using(ProductMapperImpl::new);
    instanceSupplier = instanceSupplier.andThen(ProductMapperImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
