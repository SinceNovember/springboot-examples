package com.simple.rabbitmq.origin.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
文档：RabbitMQ （二）工作队列.note
链接：http://note.youdao.com/noteshare?id=cd07deb58a02b6fa0b179193dfe91077&sub=5DC253C39CDD418CA40FA1B27770F427

*工作队列的主要任务是：避免立刻执行资源密集型任务，然后必须等待其完成。相反地，我们进行任务调度：我们把任务封装为消息发送给队列。
*工作进行在后台运行并不断的从队列中取出任务然后执行。当你运行了多个工作进程时，任务队列中的任务将会被工作进程共享执行。
* 我们使用Thread.sleep来模拟耗时的任务。我们在发送到队列的消息的末尾添加一定数量的点，每个点代表在工作线程中需要耗时1秒，例如hello…将会需要等待3秒。
*/
public class SendTask {
    //队列名称
    private final static String QUEUE_NAME = "workqueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //发送10条消息，依次在消息后面附加1-10个点
        for (int i = 0; i < 10; i++)
        {
            String dots = "";
            for (int j = 0; j <= i; j++)
            {
                dots += ".";
            }
            String message = "helloworld" + dots + dots.length();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        //关闭频道和资源
        channel.close();
        connection.close();

    }
}
