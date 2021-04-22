package com.simple.rabbitmq.origin.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.Random;

/*
*INFO 路由key
* 接受日志端
*/
public class ReceiveLogsDirect_info {

    private static final String EXCHANGE_NAME = "ex_logs_direct";
    private static final String[] SEVERITIES = { "info", "warning", "error" };

    public static void main(String[] argv) throws IOException,
            InterruptedException, TimeoutException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明direct类型转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queueName = channel.queueDeclare().getQueue();
        String severity = SEVERITIES[0];
        // 指定binding_key
        channel.queueBind(queueName, EXCHANGE_NAME, severity);
        System.out.println(" [*] Waiting for "+severity+" logs. To exit press CTRL+C");

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
                System.out.println("save message to local.." + msg);
            }
        };
        channel.basicConsume(queueName, true, consumer);

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
