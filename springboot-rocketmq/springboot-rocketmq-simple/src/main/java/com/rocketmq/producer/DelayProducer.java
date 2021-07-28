package com.rocketmq.producer;

import com.rocketmq.message.Demo03Message;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 延迟消息生产者
 */
@Component
public class DelayProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送同步消息
     * @param id
     * @param delayLevel
     * @return
     */
    public SendResult syncSendDelay(Integer id, int delayLevel) {
        Demo03Message demo03Message = new Demo03Message();
        demo03Message.setId(id);
        // 创建 Demo03Message 消息
        Message message = MessageBuilder.withPayload(demo03Message)
                .build();
        // 同步发送消息
        return rocketMQTemplate.syncSend(Demo03Message.TOPIC, message, 30 * 1000,
                delayLevel);
    }

    /**
     * 发送异步消息
     * @param id
     * @param delayLevel
     * @param callback
     */
    public void asyncSendDelay(Integer id, int delayLevel, SendCallback callback) {
        Demo03Message demo03Message = new Demo03Message();
        demo03Message.setId(id);
        // 创建 Demo03Message 消息
        Message message = MessageBuilder.withPayload(demo03Message)
                .build();
        // 同步发送消息
        rocketMQTemplate.asyncSend(Demo03Message.TOPIC, message, callback, 30 * 1000,
                delayLevel);
    }
}
