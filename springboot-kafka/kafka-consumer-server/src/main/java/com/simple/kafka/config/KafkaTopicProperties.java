package com.simple.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@ConfigurationProperties("kafka.topic")
public class KafkaTopicProperties implements Serializable {

    private String groupId;

    private String[] topicName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getTopicName() {
        return topicName;
    }

    public void setTopicName(String[] topicName) {
        this.topicName = topicName;
    }
}
