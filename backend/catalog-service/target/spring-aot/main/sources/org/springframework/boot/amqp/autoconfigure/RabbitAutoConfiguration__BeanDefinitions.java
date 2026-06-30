package org.springframework.boot.amqp.autoconfigure;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.ResourceLoader;

/**
 * Bean definitions for {@link RabbitAutoConfiguration}.
 */
@Generated
public class RabbitAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'rabbitAutoConfiguration'.
   */
  public static BeanDefinition getRabbitAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(RabbitAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Bean definitions for {@link RabbitAutoConfiguration.RabbitMessagingTemplateConfiguration}.
   */
  @Generated
  public static class RabbitMessagingTemplateConfiguration {
    /**
     * Get the bean definition for 'rabbitMessagingTemplateConfiguration'.
     */
    public static BeanDefinition getRabbitMessagingTemplateConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitAutoConfiguration.RabbitMessagingTemplateConfiguration.class);
      beanDefinition.setInstanceSupplier(RabbitAutoConfiguration.RabbitMessagingTemplateConfiguration::new);
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitMessagingTemplate'.
     */
    private static BeanInstanceSupplier<RabbitMessagingTemplate> getRabbitMessagingTemplateInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<RabbitMessagingTemplate>forFactoryMethod(RabbitAutoConfiguration.RabbitMessagingTemplateConfiguration.class, "rabbitMessagingTemplate", RabbitTemplate.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitMessagingTemplateConfiguration", RabbitAutoConfiguration.RabbitMessagingTemplateConfiguration.class).rabbitMessagingTemplate(args.get(0)));
    }

    /**
     * Get the bean definition for 'rabbitMessagingTemplate'.
     */
    public static BeanDefinition getRabbitMessagingTemplateBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitMessagingTemplate.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitMessagingTemplateConfiguration");
      beanDefinition.setInstanceSupplier(getRabbitMessagingTemplateInstanceSupplier());
      return beanDefinition;
    }
  }

  /**
   * Bean definitions for {@link RabbitAutoConfiguration.RabbitConnectionFactoryCreator}.
   */
  @Generated
  public static class RabbitConnectionFactoryCreator {
    /**
     * Get the bean instance supplier for 'org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator'.
     */
    private static BeanInstanceSupplier<RabbitAutoConfiguration.RabbitConnectionFactoryCreator> getRabbitConnectionFactoryCreatorInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<RabbitAutoConfiguration.RabbitConnectionFactoryCreator>forConstructor(RabbitProperties.class)
              .withGenerator((registeredBean, args) -> new RabbitAutoConfiguration.RabbitConnectionFactoryCreator(args.get(0)));
    }

    /**
     * Get the bean definition for 'rabbitConnectionFactoryCreator'.
     */
    public static BeanDefinition getRabbitConnectionFactoryCreatorBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class);
      beanDefinition.setInstanceSupplier(getRabbitConnectionFactoryCreatorInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitConnectionDetails'.
     */
    private static BeanInstanceSupplier<RabbitConnectionDetails> getRabbitConnectionDetailsInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<RabbitConnectionDetails>forFactoryMethod(RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class, "rabbitConnectionDetails", ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator", RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class).rabbitConnectionDetails(args.get(0)));
    }

    /**
     * Get the bean definition for 'rabbitConnectionDetails'.
     */
    public static BeanDefinition getRabbitConnectionDetailsBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitConnectionDetails.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator");
      beanDefinition.setInstanceSupplier(getRabbitConnectionDetailsInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitConnectionFactoryBeanConfigurer'.
     */
    private static BeanInstanceSupplier<RabbitConnectionFactoryBeanConfigurer> getRabbitConnectionFactoryBeanConfigurerInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<RabbitConnectionFactoryBeanConfigurer>forFactoryMethod(RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class, "rabbitConnectionFactoryBeanConfigurer", ResourceLoader.class, RabbitConnectionDetails.class, ObjectProvider.class, ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator", RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class).rabbitConnectionFactoryBeanConfigurer(args.get(0), args.get(1), args.get(2), args.get(3)));
    }

    /**
     * Get the bean definition for 'rabbitConnectionFactoryBeanConfigurer'.
     */
    public static BeanDefinition getRabbitConnectionFactoryBeanConfigurerBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitConnectionFactoryBeanConfigurer.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator");
      beanDefinition.setInstanceSupplier(getRabbitConnectionFactoryBeanConfigurerInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitConnectionFactoryConfigurer'.
     */
    private static BeanInstanceSupplier<CachingConnectionFactoryConfigurer> getRabbitConnectionFactoryConfigurerInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<CachingConnectionFactoryConfigurer>forFactoryMethod(RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class, "rabbitConnectionFactoryConfigurer", RabbitConnectionDetails.class, ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator", RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class).rabbitConnectionFactoryConfigurer(args.get(0), args.get(1)));
    }

    /**
     * Get the bean definition for 'rabbitConnectionFactoryConfigurer'.
     */
    public static BeanDefinition getRabbitConnectionFactoryConfigurerBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(CachingConnectionFactoryConfigurer.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator");
      beanDefinition.setInstanceSupplier(getRabbitConnectionFactoryConfigurerInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitConnectionFactory'.
     */
    private static BeanInstanceSupplier<CachingConnectionFactory> getRabbitConnectionFactoryInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<CachingConnectionFactory>forFactoryMethod(RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class, "rabbitConnectionFactory", RabbitConnectionFactoryBeanConfigurer.class, CachingConnectionFactoryConfigurer.class, ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator", RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class).rabbitConnectionFactory(args.get(0), args.get(1), args.get(2)));
    }

    /**
     * Get the bean definition for 'rabbitConnectionFactory'.
     */
    public static BeanDefinition getRabbitConnectionFactoryBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(CachingConnectionFactory.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitConnectionFactoryCreator");
      beanDefinition.setInstanceSupplier(getRabbitConnectionFactoryInstanceSupplier());
      return beanDefinition;
    }
  }

  /**
   * Bean definitions for {@link RabbitAutoConfiguration.RabbitTemplateConfiguration}.
   */
  @Generated
  public static class RabbitTemplateConfiguration {
    /**
     * Get the bean definition for 'rabbitTemplateConfiguration'.
     */
    public static BeanDefinition getRabbitTemplateConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitAutoConfiguration.RabbitTemplateConfiguration.class);
      beanDefinition.setInstanceSupplier(RabbitAutoConfiguration.RabbitTemplateConfiguration::new);
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitTemplateConfigurer'.
     */
    private static BeanInstanceSupplier<RabbitTemplateConfigurer> getRabbitTemplateConfigurerInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<RabbitTemplateConfigurer>forFactoryMethod(RabbitAutoConfiguration.RabbitTemplateConfiguration.class, "rabbitTemplateConfigurer", RabbitProperties.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitTemplateConfiguration", RabbitAutoConfiguration.RabbitTemplateConfiguration.class).rabbitTemplateConfigurer(args.get(0), args.get(1), args.get(2), args.get(3)));
    }

    /**
     * Get the bean definition for 'rabbitTemplateConfigurer'.
     */
    public static BeanDefinition getRabbitTemplateConfigurerBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitTemplateConfigurer.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitTemplateConfiguration");
      beanDefinition.setInstanceSupplier(getRabbitTemplateConfigurerInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'rabbitTemplate'.
     */
    private static BeanInstanceSupplier<RabbitTemplate> getRabbitTemplateInstanceSupplier() {
      return BeanInstanceSupplier.<RabbitTemplate>forFactoryMethod(RabbitAutoConfiguration.RabbitTemplateConfiguration.class, "rabbitTemplate", RabbitTemplateConfigurer.class, ConnectionFactory.class, ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitTemplateConfiguration", RabbitAutoConfiguration.RabbitTemplateConfiguration.class).rabbitTemplate(args.get(0), args.get(1), args.get(2)));
    }

    /**
     * Get the bean definition for 'rabbitTemplate'.
     */
    public static BeanDefinition getRabbitTemplateBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(RabbitTemplate.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitTemplateConfiguration");
      beanDefinition.setInstanceSupplier(getRabbitTemplateInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'amqpAdmin'.
     */
    private static BeanInstanceSupplier<AmqpAdmin> getAmqpAdminInstanceSupplier() {
      return BeanInstanceSupplier.<AmqpAdmin>forFactoryMethod(RabbitAutoConfiguration.RabbitTemplateConfiguration.class, "amqpAdmin", ConnectionFactory.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitTemplateConfiguration", RabbitAutoConfiguration.RabbitTemplateConfiguration.class).amqpAdmin(args.get(0)));
    }

    /**
     * Get the bean definition for 'amqpAdmin'.
     */
    public static BeanDefinition getAmqpAdminBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(AmqpAdmin.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration$RabbitTemplateConfiguration");
      beanDefinition.setInstanceSupplier(getAmqpAdminInstanceSupplier());
      return beanDefinition;
    }
  }
}
