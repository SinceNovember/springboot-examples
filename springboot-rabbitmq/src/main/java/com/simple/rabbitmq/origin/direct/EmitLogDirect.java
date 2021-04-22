package com.simple.rabbitmq.origin.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/*
文档：RabbitMQ （四） 路由选择 (Routing).n...
链接：http://note.youdao.com/noteshare?id=c503a1c1101f211facee2bd5ab6de64d&sub=32C4444AD45F49B08713E9DB72E2FD30

* 路由直连模式
* 我们将会使用direct类型的转发器进行替代。
* direct类型的转发器背后的路由转发算法很简单：消息会被推送至绑定键（binding key）和消息发布附带的选择键（routing key）完全匹配的队列。
*/
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "ex_logs_direct";
    private static final String[] SEVERITIES = { "info", "warning", "error" };

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器的类型
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //发送6条消息
        for (int i = 0; i < 6; i++)
        {
            String severity = getSeverity();
            String message = severity + "_log :" + UUID.randomUUID().toString();
            // 发布消息至转发器，指定routingkey
            channel.basicPublish(EXCHANGE_NAME, severity, null, message
                    .getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();

    }

    /**
     * 随机产生一种日志类型
     *
     * @return
     */
    private static String getSeverity()
    {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        return SEVERITIES[ranVal];
    }

}
