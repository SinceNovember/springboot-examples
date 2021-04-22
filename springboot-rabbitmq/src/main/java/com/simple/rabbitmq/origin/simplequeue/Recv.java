package com.simple.rabbitmq.origin.simplequeue;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
* 消息接收方
*/
public class Recv {
    //队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

            //打开连接和创建频道，与发送端一样
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列，如果先启动发送端，可不申明。
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            //创建队列消费者
            DefaultConsumer consumer = new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        //交换机
                        String exchange = envelope.getExchange();
                        //路由key
                        String routingKey = envelope.getRoutingKey();
                        //消息id
                        long deliveryTag = envelope.getDeliveryTag();
                        //消息内容
                        String msg = new String(body,"utf-8");
                        System.out.println("receive message1.." + msg);
                    }
                };
            /**
             * 监听队列String queue, boolean autoAck,Consumer callback
             * 参数明细
             * 1、队列名称
             * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
             * 3、消费消息的方法，消费者接收到消息后调用此方法
             */
            channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
