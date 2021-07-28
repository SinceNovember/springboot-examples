package com.event.service;

import com.event.event.Demo04Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderlyProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo04Message 消息
        Demo04Message message = new Demo04Message();
        message.setId(id);
        // 同步发送消息
        jmsTemplate.convertAndSend(this.getQueue(id), message);
    }

    private String getQueue(Integer id) {
        return Demo04Message.QUEUE_BASE + (id % Demo04Message.QUEUE_COUNT);
    }

}