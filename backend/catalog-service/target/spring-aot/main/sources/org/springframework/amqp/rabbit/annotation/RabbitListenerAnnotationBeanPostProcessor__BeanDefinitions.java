package org.springframework.amqp.rabbit.annotation;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RabbitListenerAnnotationBeanPostProcessor}.
 */
@Generated
public class RabbitListenerAnnotationBeanPostProcessor__BeanDefinitions {
  /**
   * Get the bean definition for 'internalRabbitListenerAnnotationProcessor'.
   */
  public static BeanDefinition getInternalRabbitListenerAnnotationProcessorBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitListenerAnnotationBeanPostProcessor.class);
    beanDefinition.setInstanceSupplier(RabbitListenerAnnotationBeanPostProcessor::new);
    return beanDefinition;
  }
}
