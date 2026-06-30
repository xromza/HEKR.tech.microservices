package org.springframework.boot.amqp.autoconfigure;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RabbitProperties}.
 */
@Generated
public class RabbitProperties__BeanDefinitions {
  /**
   * Get the bean definition for 'rabbitProperties'.
   */
  public static BeanDefinition getRabbitPropertiesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitProperties.class);
    beanDefinition.setInstanceSupplier(RabbitProperties::new);
    return beanDefinition;
  }
}
