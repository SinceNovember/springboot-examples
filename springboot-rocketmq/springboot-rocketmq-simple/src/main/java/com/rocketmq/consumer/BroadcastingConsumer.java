package com.rocketmq.consumer;

import com.rocketmq.message.Demo05Message;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 广播消费者，每个实例都获取消息
 */
@Component
@RocketMQMessageListener(
        topic = Demo05Message.TOPIC,
        consumerGroup = "demo05-consumer-group-" + Demo05Message.TOPIC,
        messageModel = MessageModel.BROADCASTING // 设置为广播消费
)
public class BroadcastingConsumer implements RocketMQListener<Demo05Message> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(Demo05Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
