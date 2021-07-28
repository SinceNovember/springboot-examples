package com.rocketmq.consumer;

import com.rocketmq.message.Demo02Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 批量消息消费者
 */
@Component
@RocketMQMessageListener(
        topic = Demo02Message.TOPIC,
        consumerGroup = "demo02-consumer-group-" + Demo02Message.TOPIC
)
public class BatchConsumer implements RocketMQListener<Demo02Message> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(Demo02Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}