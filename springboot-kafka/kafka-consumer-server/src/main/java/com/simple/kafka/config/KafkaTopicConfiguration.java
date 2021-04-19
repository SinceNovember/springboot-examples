package com.simple.kafka.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;

import javax.annotation.Resource;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties(KafkaTopicProperties.class)
public class KafkaTopicConfiguration {

    @Resource
    private ConsumerFactory consumerFactory;

    private final KafkaTopicProperties properties;

    public KafkaTopicConfiguration(KafkaTopicProperties properties) {
        this.properties = properties;
    }

    @Bean
    public String[] kafkaTopicName() {
        return properties.getTopicName();
    }

    @Bean
    public String topicGroupId() {
        return properties.getGroupId();
    }

    /**
     * 处理消息接受异常
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return (message, exception, consumer) ->{
            System.out.println("消费异常："+message.getPayload());
            return null;
        };
    }

    /**
     * 消息过滤Bean
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Objects.equals("666666", consumerRecord.value().toString())) {
                return false;
            }
            //返回true消息则被过滤
            return true;
        });
        return factory;

    }
}
