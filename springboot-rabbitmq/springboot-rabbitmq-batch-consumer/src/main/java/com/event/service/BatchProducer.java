package com.event.service;


import com.event.event.Demo05Message;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchProducer {

    //模板换为批量模板
    @Autowired
    private BatchingRabbitTemplate batchingRabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo05Message 消息
        Demo05Message message = new Demo05Message();
        message.setId(id);
        // 同步发送消息
        batchingRabbitTemplate.convertAndSend(Demo05Message.EXCHANGE, Demo05Message.ROUTING_KEY, message);
    }

}