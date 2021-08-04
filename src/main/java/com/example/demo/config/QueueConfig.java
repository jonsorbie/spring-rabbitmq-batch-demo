package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
public class QueueConfig {

    public static final String EXCHANGE_NAME = "demo-exchange";
    public static final String QUEUE_NAME = "spring-batch-amqp-demo";
    public static final String BINDING = "DEMO.#";


    @Bean
    public Exchange demoExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }


    @Bean
    public Queue demoQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }


    @Bean
    public Declarables queueBindings() {
        return new Declarables(
            new Binding(QUEUE_NAME, QUEUE, EXCHANGE_NAME, BINDING, null)
        );
    }
}
