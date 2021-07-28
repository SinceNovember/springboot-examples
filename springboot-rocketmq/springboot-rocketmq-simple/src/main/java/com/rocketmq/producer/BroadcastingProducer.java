package com.rocketmq.producer;

import com.rocketmq.message.Demo05Message;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 广播消息生产者，每个实例都获取消息
 */
@Component

public class BroadcastingProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public SendResult syncSend(Integer id) {
        // 创建 Demo05Message 消息
        Demo05Message message = new Demo05Message();
        message.setId(id);
        // 同步发送消息
        return rocketMQTemplate.syncSend(Demo05Message.TOPIC, message);
    }

}
