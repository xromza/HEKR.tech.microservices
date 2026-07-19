package ru.xromza.warehouse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.JacksonJavaTypeMapper.TypePrecedence;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.xromza.warehouse.event.OrderCreatedEvent;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter(JsonMapper jsonMapper) {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter(jsonMapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.xromza.*");

        classMapper.setIdClassMapping(Map.of(
            "ru.xromza.order_worker.event.OrderCreatedEvent", OrderCreatedEvent.class
        ));

        converter.setClassMapper(classMapper);
        converter.setTypePrecedence(TypePrecedence.TYPE_ID);
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    @Bean
    public TopicExchange warehouseEventExchange() {
        return new TopicExchange("warehouse.events.exchange");
    }

    @Bean
    public Queue warehouseOrderQueue() {
        return new Queue("warehouse.order-submitted.queue", true);
    }

    @Bean
    public Binding bindingWarehouseOrder(Queue warehouseOrderQueue, TopicExchange warehouseEventExchange) {
        return BindingBuilder
                .bind(warehouseOrderQueue)
                .to(warehouseEventExchange)
                .with("order.created");
    }

}