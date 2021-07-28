package com.simple.kafka.consumer;

import com.simple.kafka.message.Demo09Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class AckConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = Demo09Message.TOPIC,
            groupId = "demo09-consumer-group-" + Demo09Message.TOPIC)
    public void onMessage(Demo09Message message, Acknowledgment acknowledgment) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        // 提交消费进度
        if (message.getId() % 2 == 1) {
            acknowledgment.acknowledge();
        }
    }

}