package com.simple.rabbitmq.origin.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    //队列名称
    private final static String QUEUE_NAME = "workqueue";

    //打开应答机制,开启手动应答，防止在服务端发送完信息消除信息后，客户端直接断开导致消息丢失。
    private final static boolean ack = false ;


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException{
        //区分不同工作进程的输出
        final int hashCode = Recv1.class.hashCode();
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(hashCode
                + " [*] Waiting for messages. To exit press CTRL+C");

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println(hashCode + " [x] Received '" + message + "'");
                try {
                    doWork(message);
                    System.out.println(hashCode + " [x] Done");
                    //另外需要在每次处理完成一个消息后，手动发送一次应答。
                    getChannel().basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //ack关闭自动回复
        channel.basicConsume(QUEUE_NAME, ack, consumer);


    }

     /* 每个点耗时1s
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException
    {
        for (char ch : task.toCharArray())
        {
            if (ch == '.')
                Thread.sleep(1000);
        }
    }
}
