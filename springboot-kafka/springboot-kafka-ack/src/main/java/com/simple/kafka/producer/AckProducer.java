package com.simple.kafka.producer;

import com.simple.kafka.message.Demo09Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Component
public class AckProducer {


    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult syncSend(Integer id) throws ExecutionException, InterruptedException {
        // 创建 Demo08Message 消息
        Demo09Message message = new Demo09Message();
        message.setId(id);
        // 同步发送消息
        return kafkaTemplate.send(Demo09Message.TOPIC, message).get();
    }

}