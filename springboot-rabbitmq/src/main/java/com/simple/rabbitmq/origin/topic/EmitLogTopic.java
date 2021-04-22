package com.simple.rabbitmq.origin.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.UUID;

/*
文档：RabbitMQ （五）主题（Topic）.note
链接：http://note.youdao.com/noteshare?id=de0e8d9cc7a1fd9304c5e1d4998092e0&sub=3F4DF7B41D2F4541AAED5BDB881D6036

主题转发（Topic Exchange）
发往主题类型的转发器的消息不能随意的设置选择键（routing_key），必须是由点隔开的一系列的标识符组成。标识符可以是任何东西，但是一般都与消息的某些特性相关。一些合法的选择键的例子："stock.usd.nyse", "nyse.vmw","quick.orange.rabbit".你可以定义任何数量的标识符，上限为255个字节。
绑定键和选择键的形式一样。主题类型的转发器背后的逻辑和直接类型的转发器很类似：一个附带特殊的选择键将会被转发到绑定键与之匹配的队列中。需要注意的是：关于绑定键有两种特殊的情况。
*可以匹配一个标识符。#可以匹配0个或多个标识符。
*
*
*/
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception
    {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String[] routing_keys = new String[] { "kernel.info", "cron.warning",
                "auth.info", "kernel.critical" };
        for (String routing_key : routing_keys)
        {
            String msg = UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME, routing_key, null, msg
                    .getBytes());
            System.out.println(" [x] Sent routingKey = "+routing_key+" ,msg = " + msg + ".");
        }

        channel.close();
        connection.close();
    }
}
