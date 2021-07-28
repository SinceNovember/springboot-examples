package com.event.controller;

// Demo07DeadConsumer.java

import com.event.event.Demo07Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = Demo07Message.DEAD_QUEUE)
public class RetryDeadConsumer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitHandler
    public void onMessage(Demo07Message message) {
        logger.info("[onMessage][【死信队列】线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
