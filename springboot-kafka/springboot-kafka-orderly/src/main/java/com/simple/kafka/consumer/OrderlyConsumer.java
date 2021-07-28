package com.simple.kafka.consumer;

import com.simple.kafka.message.Demo07Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderlyConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = Demo07Message.TOPIC,
            groupId = "demo07-consumer-group-" + Demo07Message.TOPIC)
    public void onMessage(Demo07Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}