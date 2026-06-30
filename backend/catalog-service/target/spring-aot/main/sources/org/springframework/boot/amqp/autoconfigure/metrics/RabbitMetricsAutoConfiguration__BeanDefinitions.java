package org.springframework.boot.amqp.autoconfigure.metrics;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;

/**
 * Bean definitions for {@link RabbitMetricsAutoConfiguration}.
 */
@Generated
public class RabbitMetricsAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'rabbitMetricsAutoConfiguration'.
   */
  public static BeanDefinition getRabbitMetricsAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitMetricsAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(RabbitMetricsAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'rabbitConnectionFactoryMetricsPostProcessor'.
   */
  private static BeanInstanceSupplier<RabbitConnectionFactoryMetricsPostProcessor> getRabbitConnectionFactoryMetricsPostProcessorInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<RabbitConnectionFactoryMetricsPostProcessor>forFactoryMethod(RabbitMetricsAutoConfiguration.class, "rabbitConnectionFactoryMetricsPostProcessor", ApplicationContext.class)
            .withGenerator((registeredBean, args) -> RabbitMetricsAutoConfiguration.rabbitConnectionFactoryMetricsPostProcessor(args.get(0)));
  }

  /**
   * Get the bean definition for 'rabbitConnectionFactoryMetricsPostProcessor'.
   */
  public static BeanDefinition getRabbitConnectionFactoryMetricsPostProcessorBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitMetricsAutoConfiguration.class);
    beanDefinition.setTargetType(RabbitConnectionFactoryMetricsPostProcessor.class);
    beanDefinition.setInstanceSupplier(getRabbitConnectionFactoryMetricsPostProcessorInstanceSupplier());
    return beanDefinition;
  }
}
