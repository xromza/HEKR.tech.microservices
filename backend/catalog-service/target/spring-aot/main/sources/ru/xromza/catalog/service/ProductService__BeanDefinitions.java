package ru.xromza.catalog.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import ru.xromza.catalog.mapper.ProductCatalogResponseMapper;
import ru.xromza.catalog.mapper.ProductMapper;
import ru.xromza.catalog.repository.ProductRepository;

/**
 * Bean definitions for {@link ProductService}.
 */
@Generated
public class ProductService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'productService'.
   */
  private static BeanInstanceSupplier<ProductService> getProductServiceInstanceSupplier() {
    return BeanInstanceSupplier.<ProductService>forConstructor(ProductRepository.class, ProductMapper.class, ProductCatalogResponseMapper.class, CategoryService.class)
            .withGenerator((registeredBean, args) -> new ProductService(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'productService'.
   */
  public static BeanDefinition getProductServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductService.class);
    beanDefinition.setInstanceSupplier(getProductServiceInstanceSupplier());
    return beanDefinition;
  }
}
