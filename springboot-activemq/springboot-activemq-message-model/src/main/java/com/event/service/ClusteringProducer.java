package com.event.service;

import com.event.config.ActiveMQConfig;
import com.event.event.ClusteringMessage;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ClusteringProducer {

    @Resource(name = ActiveMQConfig.CLUSTERING_JMS_TEMPLATE_BEAN_NAME)
    private JmsMessagingTemplate jmsTemplate;

    public void syncSend(Integer id) {
        // 创建 ClusteringMessage 消息
        ClusteringMessage message = new ClusteringMessage();
        message.setId(id);
        // 同步发送消息
        jmsTemplate.convertAndSend(ClusteringMessage.QUEUE, message);
    }

}