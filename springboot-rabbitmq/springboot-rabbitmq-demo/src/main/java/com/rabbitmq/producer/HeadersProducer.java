package com.rabbitmq.producer;

import com.rabbitmq.message.Demo04Message;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeadersProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id, String headerValue) {
        // 创建 MessageProperties 属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader(Demo04Message.HEADER_KEY, headerValue); // 设置 header
        Demo04Message demo04Message = new Demo04Message();
        demo04Message.setId(id);
        // 创建 Message 消息
        Message message = rabbitTemplate.getMessageConverter().toMessage(demo04Message
                , messageProperties);
        // 同步发送消息
        rabbitTemplate.send(Demo04Message.EXCHANGE, null, message);
    }
}
