package com.rabbitmq.consumer;

import com.rabbitmq.message.Demo01Message;
import com.rabbitmq.message.Demo02Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = Demo02Message.QUEUE)
public class TopicConsumer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitHandler
    public void onMessage(Demo02Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    /**
     * 可获取RoutingKey、创建时间等等信息
     */
//    @RabbitHandler(isDefault = true)
//    public void onMessage(org.springframework.amqp.core.Message message) {
//        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
//    }

}
