package com.event.controller;

import com.event.event.Demo15Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 手动确认消息消费
 */
@Component
@RabbitListener(queues = Demo15Message.QUEUE)
public class ConverterConsumer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitHandler(isDefault = true)
    public void onMessage(Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(),
                new String(message.getBody()));
    }
}