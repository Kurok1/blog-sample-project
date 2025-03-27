package com.example.common;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class RabbitDeclaredConfiguration {
    @Bean("order-exchange")
    public Exchange orderExchange() {
        return new DirectExchange("order-exchange", true, false);
    }

    @Bean("order")
    public Queue orderQueue() {
        return new Queue("order", true, false, false, Map.of("x-queue-type", "quorum"));
    }

    @Bean("order-binding")
    public Binding orderBinding(@Qualifier("order-exchange") Exchange orderExchange,
                             @Qualifier("order") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(orderExchange)
                .with("order-routing-key")
                .noargs();
    }


    @Bean("order-callback-exchange")
    public Exchange orderCallbackExchange() {
        return new DirectExchange("order-callback-exchange", true, false);
    }

    @Bean("order-callback")
    public Queue orderCallbackQueue() {
        return new Queue("order-callback", true, false, false, Map.of("x-queue-type", "quorum"));
    }

    @Bean("order-callback-binding")
    public Binding orderCallbackbinding(@Qualifier("order-callback-exchange") Exchange orderExchange,
                             @Qualifier("order-callback") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(orderExchange)
                .with("order-callback-routing-key")
                .noargs();
    }
}
