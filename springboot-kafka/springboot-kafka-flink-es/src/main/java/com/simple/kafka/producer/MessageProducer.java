package com.simple.kafka.producer;

import com.simple.kafka.message.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Component
public class MessageProducer {

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult syncSend(Integer id) throws ExecutionException, InterruptedException {
        // 创建 Demo01Message 消息
        Message message = new Message();
        message.setId(id.toString());
        message.setTitle("title");
        // 同步发送消息
        return kafkaTemplate.send(Message.TOPIC, message).get();
    }

    public ListenableFuture<SendResult<Object, Object>> asyncSend(Integer id) {
        // 创建 Demo01Message 消息
        Message message = new Message();
        message.setId(id.toString());
        message.setTitle("title");
        // 异步发送消息
        return kafkaTemplate.send(Message.TOPIC, message);
    }

}
