package com.event.service;


import com.event.event.Demo06Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo06Message 消息
        Demo06Message message = new Demo06Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo06Message.EXCHANGE, Demo06Message.ROUTING_KEY, message);
    }

}