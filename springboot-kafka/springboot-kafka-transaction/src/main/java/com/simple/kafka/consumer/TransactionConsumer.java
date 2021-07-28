package com.simple.kafka.consumer;

import com.simple.kafka.message.Demo08Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TransactionConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = Demo08Message.TOPIC,
            groupId = "demo08-consumer-group-" + Demo08Message.TOPIC)
    public void onMessage(Demo08Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}