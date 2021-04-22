package com.simple.rabbitmq.origin.fanout;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/*
 *接收信息用于保存端
 * 随机创建一个队列，然后将队列与转发器绑定，然后将消费者与该队列绑定，然后写入日志文件。
 */
public class ReceiveLogsToSave {
    private final static String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//
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
                System.out.println("save message to local.." + msg);
                //将日志保存到本地
                print2File(msg);
            }
        };

            channel.basicConsume("log2", true, consumer);
    }

    private static void print2File(String msg) {
        try {
            String dir = ReceiveLogsToSave.class.getClassLoader().getResource("").getPath();
            String logFileName = new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date());
            File file = new File(dir, logFileName + ".txt");
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write((msg + "\r\n").getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
