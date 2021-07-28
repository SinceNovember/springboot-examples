package com.simple.kafka.consumer;

import com.simple.kafka.message.Demo02Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = Demo02Message.TOPIC,
            groupId = "demo02-consumer-group-" + Demo02Message.TOPIC)
    public void onMessage(List<Demo02Message> messages) {
        logger.info("[onMessage][线程编号:{} 消息数量：{}]", Thread.currentThread().getId(), messages.size());
    }
}