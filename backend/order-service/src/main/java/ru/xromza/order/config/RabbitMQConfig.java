package ru.xromza.order.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJavaTypeMapper.TypePrecedence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.xromza.order.event.OrderStatusUpdatedEvent;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {
    @Bean
    public MessageConverter messageConverter(JsonMapper jsonMapper) {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter(jsonMapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.xromza.*");

        classMapper.setIdClassMapping(Map.of(
                "ru.xromza.order_worker.event.OrderStatusUpdatedEvent", OrderStatusUpdatedEvent.class));
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
    public TopicExchange orderExchange() {
        return new TopicExchange("order.submission.exchange");
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("order.submission.queue", true);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with("order.submitted");
    }

    @Bean
    public TopicExchange orderStatusExchange() {
        return new TopicExchange("order.status.exchange");
    }

    @Bean
    public Queue orderStatusQueue() {
        return new Queue("order.status.queue");
    }

    @Bean
    public Binding orderStatusBinding(TopicExchange orderStatusExchange, Queue orderStatusQueue) {
        return BindingBuilder
                .bind(orderStatusQueue)
                .to(orderStatusExchange)
                .with("order.status.updated");
    }
}
