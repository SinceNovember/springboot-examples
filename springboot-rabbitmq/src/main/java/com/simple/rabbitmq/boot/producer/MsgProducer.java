package com.simple.rabbitmq.boot.producer;

import com.simple.rabbitmq.boot.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
    发送fanout消息
    */
    public void send2FanoutTestQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_FANOUT_EXCHANGE,
                "", massage);
    }

    /*
   发送Direct消息
   */
    public void send2DirectTestQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_DIRECT_EXCHANGE,
                RabbitConfig.DIRECT_ROUTINGKEY, massage);
    }

    /*
    发送Topic消息
    */
    public void send2TopicTestAQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_TOPIC_EXCHANGE,
                "test.aaa", massage);
    }

    public void send2TopicTestBQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_TOPIC_EXCHANGE,
                "test.bbb", massage);
    }

}
