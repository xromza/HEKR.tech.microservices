package ru.xromza.catalog.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import ru.xromza.catalog.mapper.CategoryRequestMapper;
import ru.xromza.catalog.mapper.CategoryResponseMapper;
import ru.xromza.catalog.repository.CategoryRepository;

/**
 * Bean definitions for {@link CategoryService}.
 */
@Generated
public class CategoryService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'categoryService'.
   */
  private static BeanInstanceSupplier<CategoryService> getCategoryServiceInstanceSupplier() {
    return BeanInstanceSupplier.<CategoryService>forConstructor(CategoryRepository.class, CategoryResponseMapper.class, CategoryRequestMapper.class)
            .withGenerator((registeredBean, args) -> new CategoryService(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'categoryService'.
   */
  public static BeanDefinition getCategoryServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CategoryService.class);
    beanDefinition.setInstanceSupplier(getCategoryServiceInstanceSupplier());
    return beanDefinition;
  }
}
