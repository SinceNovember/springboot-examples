package com.simple.rabbitmq.boot.consumer;

import com.simple.rabbitmq.boot.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MsgConsumer {

    /**
     * 可以不指定交换机
     * 接受fanout路由方式消息
     * @param massage
     */
    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitConfig.FANOUT_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = RabbitConfig.TEST_FANOUT_EXCHANGE, type = "fanout"))
                    })
    @RabbitHandler
    public void processFanoutMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        System.out.println("received Fanout message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitConfig.FANOUT_QUEUE_NAME1, durable = "true"),
                                    exchange = @Exchange(value = RabbitConfig.TEST_FANOUT_EXCHANGE, type = "fanout"))
                    })
    @RabbitHandler
    public void processFanout1Msg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        System.out.println("received Fanout1 message : " + msg);
    }

    /**
     * 接受Direct直连消息，接受队列的指定key的消息
     * 只需要指定队列名即可
     * @param massage
     */
    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitConfig.DIRECT_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = RabbitConfig.TEST_DIRECT_EXCHANGE),
                                    key = RabbitConfig.DIRECT_ROUTINGKEY)
                    })
    @RabbitHandler
    public void processDirectMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        System.out.println("received Direct message : " + msg);
    }

    /**
     * 接受topic类型的队列
     * 根据路由模糊匹配
     * @param massage
     */
    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitConfig.TOPIC_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = RabbitConfig.TEST_TOPIC_EXCHANGE, type = "topic"),
                                    key = RabbitConfig.TOPIC_ROUTINGKEY)
                    })
    @RabbitHandler
    public void processTopicMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        System.out.println("received Topic message : " + msg);
    }
}
