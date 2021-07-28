package com.event.service;


import com.event.event.Demo13Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfirmProducer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo13Message 消息
        Demo13Message message = new Demo13Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo13Message.EXCHANGE, Demo13Message.ROUTING_KEY, message);
    }
}