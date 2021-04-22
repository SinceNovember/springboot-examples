package com.simple.rabbitmq.origin.topic;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsTopicForKernel {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception
    {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 随机生成一个队列
        String queueName = channel.queueDeclare().getQueue();

        // 接收所有与服务端发送的*.critical相关的消息，如(kernel.s1)
        channel.queueBind(queueName, EXCHANGE_NAME, "kernel.*");
        System.out.println(" [*] Waiting for messages about kernel. To exit press CTRL+C");

        //定义消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                String exchange = envelope.getExchange();
                //消息内容
                String message = new String(body, "utf-8");
                System.out.println(message);
            }
        };
        /**
         * 监听队列String queue, boolean autoAck,Consumer callback
         * 参数明细
         * 1、队列名称
         * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
         * 3、消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(queueName, true, defaultConsumer);
    }
}
