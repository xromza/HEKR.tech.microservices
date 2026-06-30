package org.springframework.boot.amqp.autoconfigure;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RabbitAnnotationDrivenConfiguration}.
 */
@Generated
public class RabbitAnnotationDrivenConfiguration__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration'.
   */
  private static BeanInstanceSupplier<RabbitAnnotationDrivenConfiguration> getRabbitAnnotationDrivenConfigurationInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<RabbitAnnotationDrivenConfiguration>forConstructor(ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, RabbitProperties.class)
            .withGenerator((registeredBean, args) -> new RabbitAnnotationDrivenConfiguration(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4)));
  }

  /**
   * Get the bean definition for 'rabbitAnnotationDrivenConfiguration'.
   */
  public static BeanDefinition getRabbitAnnotationDrivenConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitAnnotationDrivenConfiguration.class);
    beanDefinition.setInstanceSupplier(getRabbitAnnotationDrivenConfigurationInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'simpleRabbitListenerContainerFactoryConfigurer'.
   */
  private static BeanInstanceSupplier<SimpleRabbitListenerContainerFactoryConfigurer> getSimpleRabbitListenerContainerFactoryConfigurerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SimpleRabbitListenerContainerFactoryConfigurer>forFactoryMethod(RabbitAnnotationDrivenConfiguration.class, "simpleRabbitListenerContainerFactoryConfigurer")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration", RabbitAnnotationDrivenConfiguration.class).simpleRabbitListenerContainerFactoryConfigurer());
  }

  /**
   * Get the bean definition for 'simpleRabbitListenerContainerFactoryConfigurer'.
   */
  public static BeanDefinition getSimpleRabbitListenerContainerFactoryConfigurerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SimpleRabbitListenerContainerFactoryConfigurer.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration");
    beanDefinition.setInstanceSupplier(getSimpleRabbitListenerContainerFactoryConfigurerInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'rabbitListenerContainerFactory'.
   */
  private static BeanInstanceSupplier<SimpleRabbitListenerContainerFactory> getRabbitListenerContainerFactoryInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SimpleRabbitListenerContainerFactory>forFactoryMethod(RabbitAnnotationDrivenConfiguration.class, "simpleRabbitListenerContainerFactory", SimpleRabbitListenerContainerFactoryConfigurer.class, ConnectionFactory.class, ObjectProvider.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration", RabbitAnnotationDrivenConfiguration.class).simpleRabbitListenerContainerFactory(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'rabbitListenerContainerFactory'.
   */
  public static BeanDefinition getRabbitListenerContainerFactoryBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SimpleRabbitListenerContainerFactory.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration");
    beanDefinition.setInstanceSupplier(getRabbitListenerContainerFactoryInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'directRabbitListenerContainerFactoryConfigurer'.
   */
  private static BeanInstanceSupplier<DirectRabbitListenerContainerFactoryConfigurer> getDirectRabbitListenerContainerFactoryConfigurerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DirectRabbitListenerContainerFactoryConfigurer>forFactoryMethod(RabbitAnnotationDrivenConfiguration.class, "directRabbitListenerContainerFactoryConfigurer")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration", RabbitAnnotationDrivenConfiguration.class).directRabbitListenerContainerFactoryConfigurer());
  }

  /**
   * Get the bean definition for 'directRabbitListenerContainerFactoryConfigurer'.
   */
  public static BeanDefinition getDirectRabbitListenerContainerFactoryConfigurerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DirectRabbitListenerContainerFactoryConfigurer.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAnnotationDrivenConfiguration");
    beanDefinition.setInstanceSupplier(getDirectRabbitListenerContainerFactoryConfigurerInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Bean definitions for {@link RabbitAnnotationDrivenConfiguration.EnableRabbitConfiguration}.
   */
  @Generated
  public static class EnableRabbitConfiguration {
    /**
     * Get the bean definition for 'enableRabbitConfiguration'.
     */
    public static BeanDefinition getEnableRabbitConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitAnnotationDrivenConfiguration.EnableRabbitConfiguration.class);
      beanDefinition.setInstanceSupplier(RabbitAnnotationDrivenConfiguration.EnableRabbitConfiguration::new);
      return beanDefinition;
    }
  }
}
