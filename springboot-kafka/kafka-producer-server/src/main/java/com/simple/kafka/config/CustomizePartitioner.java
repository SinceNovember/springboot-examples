package com.simple.kafka.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义消息发送到哪个分区
 */
public class CustomizePartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 自定义分区规则(这里假设全部发到0号分区)
        // ......
        return 0;
    }
    @Override
    public void close() {
    }
    @Override
    public void configure(Map<String, ?> configs) {
    }

}
