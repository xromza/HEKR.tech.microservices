package ru.xromza.catalog.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import ru.xromza.catalog.mapper.ProductMapper;
import ru.xromza.catalog.mapper.ProductVariantMapper;
import ru.xromza.catalog.repository.ProductVariantsRepository;

/**
 * Bean definitions for {@link ProductVariantService}.
 */
@Generated
public class ProductVariantService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'productVariantService'.
   */
  private static BeanInstanceSupplier<ProductVariantService> getProductVariantServiceInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ProductVariantService>forConstructor(ProductService.class, ProductVariantMapper.class, ProductVariantsRepository.class, ProductMapper.class)
            .withGenerator((registeredBean, args) -> new ProductVariantService(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'productVariantService'.
   */
  public static BeanDefinition getProductVariantServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductVariantService.class);
    beanDefinition.setInstanceSupplier(getProductVariantServiceInstanceSupplier());
    return beanDefinition;
  }
}
