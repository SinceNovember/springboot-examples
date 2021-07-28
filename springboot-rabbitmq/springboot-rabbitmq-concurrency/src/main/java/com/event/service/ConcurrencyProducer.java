package com.event.service;


import com.event.event.Demo09Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcurrencyProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo09Message 消息
        Demo09Message message = new Demo09Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo09Message.EXCHANGE, Demo09Message.ROUTING_KEY, message);
    }

}