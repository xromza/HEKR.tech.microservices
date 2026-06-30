package org.springframework.amqp.rabbit.listener;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RabbitListenerEndpointRegistry}.
 */
@Generated
public class RabbitListenerEndpointRegistry__BeanDefinitions {
  /**
   * Get the bean definition for 'internalRabbitListenerEndpointRegistry'.
   */
  public static BeanDefinition getInternalRabbitListenerEndpointRegistryBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitListenerEndpointRegistry.class);
    beanDefinition.setInstanceSupplier(RabbitListenerEndpointRegistry::new);
    return beanDefinition;
  }
}
