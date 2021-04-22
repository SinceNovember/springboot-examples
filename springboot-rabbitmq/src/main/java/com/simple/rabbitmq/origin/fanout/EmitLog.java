package com.simple.rabbitmq.origin.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/*
文档：RabbitMQ （三） 发布-订阅.note
链接：http://note.youdao.com/noteshare?id=f619f224294ed07b4dc027cf41162d39&sub=C4DA5602C3464AADAA134E35C588613B

*日志发送端
下面列出一些可用的转发器类型：
Direct
Topic
Headers
Fanout
目前我们关注最后一个fanout，声明转发器类型的代码：
channel.exchangeDeclare("logs","fanout");
fanout类型转发器特别简单，把所有它介绍到的消息，广播到所有它所知道的队列，。不过这正是我们前述的日志系统所需要的。
* */
public class EmitLog {
    private final static String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        channel.queueDeclare("log1", true, false, false, null  );
        channel.queueDeclare("log2", true, false, false, null  );

        /**
         * 参数明细
         * 1、队列名称
         * 2、交换机名称
         * 3、路由key
         */
        channel.queueBind("log1", EXCHANGE_NAME, "");
        channel.queueBind("log2", EXCHANGE_NAME, "");

        String message = LocalDate.now().toString() + " : log something";
        /**
         * 消息发布方法
         * param1：Exchange的名称，如果没有指定，则使用Default Exchange
         * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
         * param3:消息包含的属性
         * param4：消息体
         */
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }
}
