package ru.xromza.catalog.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ProductMapperImpl}.
 */
@Generated
public class ProductMapperImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ProductMapperImpl apply(RegisteredBean registeredBean, ProductMapperImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("productVariantMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
