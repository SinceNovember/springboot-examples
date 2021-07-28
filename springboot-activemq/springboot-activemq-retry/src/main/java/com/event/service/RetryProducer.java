package com.event.service;

import com.event.event.Demo05Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetryProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void syncSend(Integer id) {
        // 创建 ClusteringMessage 消息
        Demo05Message message = new Demo05Message();
        message.setId(id);
        // 同步发送消息
        jmsTemplate.convertAndSend(Demo05Message.QUEUE, message);
    }

}