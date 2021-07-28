package com.event.service;

import com.event.config.ActiveMQConfig;
import com.event.event.BroadcastMessage;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BroadcastProducer {

    @Resource(name = ActiveMQConfig.BROADCAST_JMS_TEMPLATE_BEAN_NAME)
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void syncSend(Integer id) {
        // 创建 BroadcastMessage 消息
        BroadcastMessage message = new BroadcastMessage();
        message.setId(id);
        // 同步发送消息
        jmsMessagingTemplate.convertAndSend(BroadcastMessage.TOPIC, message);
    }

}