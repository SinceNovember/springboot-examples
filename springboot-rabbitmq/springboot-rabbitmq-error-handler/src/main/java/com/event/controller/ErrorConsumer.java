package com.event.controller;

import com.event.event.Demo16Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 手动确认消息消费
 */
@Component
@RabbitListener(queues = Demo16Message.QUEUE)
public class ErrorConsumer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitHandler
    public void onMessage(Demo16Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        // 模拟消费异常
        throw new RuntimeException("你猜");
    }
}