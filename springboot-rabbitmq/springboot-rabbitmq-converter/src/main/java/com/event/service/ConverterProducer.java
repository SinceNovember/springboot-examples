package com.event.service;


import com.event.event.Demo15Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConverterProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo01Message 消息
        Demo15Message message = new Demo15Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo15Message.EXCHANGE, Demo15Message.ROUTING_KEY, message);
    }}