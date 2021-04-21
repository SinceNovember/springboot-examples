package com.simple.activemq.original.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProduct {
    public static void main(String[] args) throws Exception {
        //连接工厂
        // 因为: 底层实现: final String defaultURL = "tcp://"+DEFAULT_BROKER_HOST+ ":" + DEFAULT_BROKER_PORT;
        // 所以: 路径 tcp://host:61616
        // 1. 创建连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        //2.创建连接
        Connection connection = connectionFactory.createConnection();
        //3.打开连接
        connection.start();
        //4.创建会话
        //第一个参数: 是否开启事务
        //第二个参数: 消息是否自动确认
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //创建队列
        Queue queue = session.createQueue("hello3");
        //5.创建生产者
        MessageProducer producer = session.createProducer(queue);
        //6.创建消息
        TextMessage message = session.createTextMessage("hi i am  liming2");
        //7.发送信息
        producer.send(message);
        //8.关闭资源
        session.commit();
        producer.close();
        session.close();
        connection.close();
        System.out.println("消息生成成功");
    }
}
