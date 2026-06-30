package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ProductVariantMapperImpl}.
 */
@Generated
public class ProductVariantMapperImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ProductVariantMapperImpl apply(RegisteredBean registeredBean,
      ProductVariantMapperImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("imageMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
