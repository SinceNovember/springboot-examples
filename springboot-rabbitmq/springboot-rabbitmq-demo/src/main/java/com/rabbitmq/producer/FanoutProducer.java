package com.rabbitmq.producer;

import com.rabbitmq.message.Demo03Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo03Message 消息
        Demo03Message message = new Demo03Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo03Message.EXCHANGE, null, message);
    }

}
