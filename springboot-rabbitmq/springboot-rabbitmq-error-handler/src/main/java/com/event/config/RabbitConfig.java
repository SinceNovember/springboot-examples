package com.event.config;

import com.event.event.Demo16Message;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// RabbitConfig.java
@Configuration
public class RabbitConfig {
    /**
     * Direct Exchange 示例的配置类
     */
    public static class DirectExchangeDemoConfiguration {

        // 创建 Queue
        @Bean
        public Queue demo15Queue() {
            return new Queue(Demo16Message.QUEUE, // Queue 名字
                    true, // durable: 是否持久化
                    false, // exclusive: 是否排它
                    false); // autoDelete: 是否自动删除
        }

        // 创建 Direct Exchange
        @Bean
        public DirectExchange demo15Exchange() {
            return new DirectExchange(Demo16Message.EXCHANGE,
                    true,  // durable: 是否持久化
                    false);  // exclusive: 是否排它
        }

        // 创建 Binding
        // Exchange：Demo15Message.EXCHANGE
        // Routing key：Demo15Message.ROUTING_KEY
        // Queue：Demo15Message.QUEUE
        @Bean
        public Binding demo15Binding() {
            return BindingBuilder.bind(demo15Queue()).to(demo15Exchange()).with(Demo16Message.ROUTING_KEY);
        }

    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}