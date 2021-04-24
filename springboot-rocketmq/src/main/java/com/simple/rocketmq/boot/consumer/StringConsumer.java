package com.simple.rocketmq.boot.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(nameServer = "${rocketmq.name-server}", topic = "${rocketmq.topic.string}", consumerGroup = "${rocketmq.consumer.group1}", selectorExpression = "${rocketmq.tag}")
public class StringConsumer implements RocketMQListener<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(String message) {
        // 重写消息处理方法
        logger.info("------- StringConsumer received:{} \n", message);
        // TODO 对消息进行处理，比如写入数据
    }
}
