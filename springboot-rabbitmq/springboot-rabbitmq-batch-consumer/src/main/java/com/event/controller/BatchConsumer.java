package com.event.controller;

import com.event.event.Demo05Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 批量消费消息
 */
@Component
@RabbitListener(queues = Demo05Message.QUEUE,
        containerFactory = "consumerBatchContainerFactory")
public class BatchConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitHandler
    public void onMessage(List<Demo05Message> messages) {
        logger.info("[onMessage][线程编号:{} 消息数量：{}]", Thread.currentThread().getId(), messages.size());
    }

}