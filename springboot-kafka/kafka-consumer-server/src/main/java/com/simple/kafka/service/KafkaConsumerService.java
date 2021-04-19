package com.simple.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KafkaConsumerService {

    private Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Resource
    private KafkaTemplate kafkaTemplate;

    /**
     * 接受指定topic的单一消息，只有在未开启批量消息时才会正常运行
     * 在同一组中，保证topic中每个分区中的消息只会给组中一个消费者消费
     * 如果指定key的话，会根据hash值寻找分区
     * 不同的话会通过轮询的方法寻找分区
     *
     * errorHandler: 因为开启了批量消费，所以这边会异常，调用consumerAwareListenerErrorHandler异常处理器
     * containerFactory:过滤只接受666666的数据
     */
    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}", errorHandler="consumerAwareListenerErrorHandler",containerFactory = "filterContainerFactory")
    public void receiveSingleMessage(ConsumerRecord<?, ?> record) {
        log.info("kafka process single sessage start");
        log.info("process single message, topic = {}, msg = {}", record.topic(), record.value());
        // do something ...
        log.info("kafka process single message end");
    }

    /**
     * 接受指定topic的批量消息
     * 在同一组中，保证一条消息只会给组中一个消费者消费
     * 如果指定key的话，会根据hash值寻找分区
     * 不同的话会通过轮询的方法寻找分区
     * 如果开启批量的话就需要统一个组中的接受信息的参数都变为List
     * containerFactory:过滤只接受666666的数据
     */
    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}", containerFactory = "filterContainerFactory")
    public void receiveBatchMessage(List<ConsumerRecord<?, ?>> records) {
        log.info(">>>监听器批量消费一次，records.size()={}", records.size());
        log.info("kafka processMessage start");
        records.forEach(record -> {
            log.info("processMessage, topic = {}, msg = {}", record.topic(), record.value());
        });
        // do something ...
        log.info("kafka processMessage end");
    }

    @KafkaListener(topics = {"testtopic"}, groupId = "#{topicGroupId}")
    public void receiveBatchMessage1(List<ConsumerRecord<?, ?>> records) {
        log.info(">>>监听器1批量消费一次，records.size()={}", records.size());

        log.info("kafka processMessage 01 start");
        records.forEach(record -> {
            log.info("processMessage, topic = {}, msg = {}", record.topic(), record.value());
        });
        // do something ...
        log.info("kafka processMessage end");
    }

    /**
     * 接受指定分区的消息
     * 同时监听testtopic和testtopic1，监听testtopic的0号分区、testtopic1的 "0号和1号" 分区，指向1号分区的offset初始值为8
     * @param record
     */
    @KafkaListener(groupId = "#{topicGroupId}", topicPartitions = {
            @TopicPartition(topic = "testtopic", partitions = {"1"}),
            @TopicPartition(topic = "testtopic1", partitions = "0", partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
    }, errorHandler = "consumerAwareListenerErrorHandler")
    public void receivePartitionSingleMessage(ConsumerRecord<Integer, String> record) {

        log.info("kafka process partition Message start");
        log.info("process partition Message, topic = {}, msg = {}", record.topic(), record.value());
        // do something ...
        log.info("kafka process partition Message end");
    }
}
