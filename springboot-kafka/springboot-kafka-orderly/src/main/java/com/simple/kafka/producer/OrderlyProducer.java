package com.simple.kafka.producer;

import com.simple.kafka.message.Demo07Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Component
public class OrderlyProducer {

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 顺序消费
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public SendResult syncSendOrderly(Integer id) throws ExecutionException, InterruptedException {
        // 创建 Demo01Message 消息
        Demo07Message message = new Demo07Message();
        message.setId(id);
        // 同步发送消息
        // 因为我们使用 String 的方式序列化 key ，所以需要将 id 转换成 String
        return kafkaTemplate.send(Demo07Message.TOPIC, String.valueOf(id), message).get();
    }

    public SendResult syncSend(Integer id) throws ExecutionException, InterruptedException {
        // 创建 Demo04Message 消息
        Demo07Message message = new Demo07Message();
        message.setId(id);
        // 同步发送消息
        return kafkaTemplate.send(Demo07Message.TOPIC, message).get();
    }

}