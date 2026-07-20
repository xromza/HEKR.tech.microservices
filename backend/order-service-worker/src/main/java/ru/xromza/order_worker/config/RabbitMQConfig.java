package ru.xromza.order_worker.config;

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

import ru.xromza.order_worker.event.InventoryResultEvent;
import ru.xromza.order_worker.event.OrderSubmitEvent;
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
                "ru.xromza.order.event.OrderSubmitEvent", OrderSubmitEvent.class,
                "ru.xromza.warehouse.event.InventoryResultEvent", InventoryResultEvent.class));

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
    public Queue warehouseResponsesQueue() {
        return new Queue("order.warehouse-responses.queue", true);
    }

    @Bean
    public Queue ordersQueue() {
        return new Queue("order.submission.queue", true);
    }

    @Bean
    public TopicExchange orderSubmissionExchange() {
        return new TopicExchange("order.submission.exchange");
    }

    @Bean
    public Binding bindingOrders(TopicExchange orderSubmissionExchange, Queue ordersQueue) {
        return BindingBuilder
                .bind(ordersQueue)
                .to(orderSubmissionExchange)
                .with("order.submitted");
    }

    @Bean
    public TopicExchange warehouseEventsExchange() {
        return new TopicExchange("warehouse.events.exchange");
    }

    @Bean
    public Binding bindingWarehouseReserved(TopicExchange warehouseEventsExchange, Queue warehouseResponsesQueue) {
        return BindingBuilder
                .bind(warehouseResponsesQueue)
                .to(warehouseEventsExchange)
                .with("inventory.*");
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