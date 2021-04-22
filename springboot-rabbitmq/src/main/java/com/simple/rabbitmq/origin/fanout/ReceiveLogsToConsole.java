package com.simple.rabbitmq.origin.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
*
*随机创建一个队列，然后将队列与转发器绑定，然后将消费者与该队列绑定，然后打印到控制台。
*/
public class ReceiveLogsToConsole {

    private final static String EXCHANGE_NAME = "ex_log";

    public static void main(String[] argv) throws IOException,
            InterruptedException, TimeoutException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//        // 创建一个非持久的、唯一的且自动删除的队列
//        String queueName = channel.queueDeclare().getQueue();
//        System.out.println(queueName);
//        // 为转发器指定队列，设置binding
//        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             * 消费者接收消息调用此方法
             * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
             * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * @param properties
             * @param body
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //路由key
                String routingKey = envelope.getRoutingKey();
                //消息id
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String msg = new String(body, "utf-8");
                System.out.println("print to console..." + msg);
            }
        };
        channel.basicConsume("log1", true, consumer);
    }
}
