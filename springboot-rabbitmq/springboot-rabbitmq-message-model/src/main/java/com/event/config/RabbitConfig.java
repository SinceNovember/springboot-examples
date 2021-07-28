package com.event.config;

import com.event.event.BroadcastMessage;
import com.event.event.ClusteringMessage;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// RabbitConfig.java
@Configuration
public class RabbitConfig {

    /**
     * 广播消费的示例的配置
     */
    public static class BroadcastingConfiguration {

        // 创建 Topic Exchange
        @Bean
        public TopicExchange broadcastingExchange() {
            return new TopicExchange(BroadcastMessage.EXCHANGE,
                    true,  // durable: 是否持久化
                    false);  // exclusive: 是否排它
        }

    }

    /**
     * 集群消费的示例的配置
     */
    public static class ClusteringConfiguration {

        // 创建 Topic Exchange
        @Bean
        public TopicExchange clusteringExchange() {
            return new TopicExchange(ClusteringMessage.EXCHANGE,
                    true,  // durable: 是否持久化
                    false);  // exclusive: 是否排它
        }

    }

}