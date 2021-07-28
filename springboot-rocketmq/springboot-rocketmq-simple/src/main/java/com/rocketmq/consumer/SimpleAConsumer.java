package com.rocketmq.consumer;

import com.rocketmq.message.Demo01Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 新增加的topic里的组会读取当前broker里的所有消息，读取消息与consumerOffset的位置有关，代表当前消费组读取的消费位置。
 * brokerOffset代表当前broker中消息所包含的消息位置
 * 不同的消费组都可以获取topic中的消息，但都有自己的Offset
 * 多个实例是通过轮询的方式平摊到各个实例中，但如过是BROADCASTING模式话，每个实例都可以获取消息
 */
@Component
@RocketMQMessageListener(
        topic = Demo01Message.TOPIC,
        consumerGroup = "demo01-A-consumer-group-" + Demo01Message.TOPIC
)
public class SimpleAConsumer implements RocketMQListener<MessageExt> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(MessageExt message) {
        logger.info("[onMessage-A][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}