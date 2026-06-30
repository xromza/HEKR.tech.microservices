package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductVariantMapperImpl}.
 */
@Generated
public class ProductVariantMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'productVariantMapperImpl'.
   */
  public static BeanDefinition getProductVariantMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductVariantMapperImpl.class);
    InstanceSupplier<ProductVariantMapperImpl> instanceSupplier = InstanceSupplier.using(ProductVariantMapperImpl::new);
    instanceSupplier = instanceSupplier.andThen(ProductVariantMapperImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
