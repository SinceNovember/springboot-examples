package com.simple.kafka.consumer;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.simple.kafka.mapper.es.DocumentEsMapper;
import com.simple.kafka.mapper.es.MessageEsMapper;
import com.simple.kafka.message.Message;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * .
 *
 * @author SinceNovember
 * @date 2022/10/24
 */
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageEsMapper messageEsMapper;

    @KafkaListener(topics = Message.TOPIC,
            groupId = "consumer-group-" + Message.TOPIC)
    public void onMessage(Message record) {
        //插入到es中
        messageEsMapper.insert(record);
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);
    }
}
