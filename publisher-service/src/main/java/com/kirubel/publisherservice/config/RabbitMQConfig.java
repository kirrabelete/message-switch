package com.kirubel.publisherservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${filtered.message.queue.name}")
    String filteredMessageQueueName;

    @Value("${all.message.queue.name}")
    String allMessageQueueName;

    @Value("${topic.exchange.name}")
    String topicExchangeName;

    @Value("${allowed.routing.key}")
    String allowedRoutingKey;


    @Bean
    Queue filteredMessageQueue() {
        return new Queue(filteredMessageQueueName, false);
    }

    @Bean
    Queue allMessageQueue() {
        return new Queue(allMessageQueueName, false);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    /* bind filteredMessageQueue with topic exchange to only submit
       messages that contain allowed routing keys */
    @Bean
    Binding filteredMessageBinding(Queue filteredMessageQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(filteredMessageQueue).to(topicExchange).with(allowedRoutingKey);
    }

    /* this binding will be used to submit all messages to the queue regardless of
       the routing key used and be consumed by the charging module */
    @Bean
    Binding allMessageBinding(Queue allMessageQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(allMessageQueue).to(topicExchange).with("#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
