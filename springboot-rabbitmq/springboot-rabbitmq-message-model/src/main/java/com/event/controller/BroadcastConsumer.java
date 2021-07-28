package com.event.controller;


import com.event.event.BroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 通过随机生成的队列名，保证每个启动的实例都具有不同的队列，这样发送发送#的key可以路由到所有队列中
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(
                        name = BroadcastMessage.QUEUE + "-" + "#{T(java.util.UUID).randomUUID()}",
                        //关闭后，队列自动删除
                        autoDelete = "true"
                ),
                exchange = @Exchange(
                        name = BroadcastMessage.EXCHANGE,
                        type = ExchangeTypes.TOPIC,
                        declare = "false"
                )
        )
)
public class BroadcastConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitHandler
    public void onMessage(BroadcastMessage message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}