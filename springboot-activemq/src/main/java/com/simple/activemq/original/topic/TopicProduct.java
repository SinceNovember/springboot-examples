package com.simple.activemq.original.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * topic类型生产者
 */
public class TopicProduct {
    public static void main(String[] args) throws JMSException {
        // 1. 创建连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        // 2. 创建连接
        Connection connection = connectionFactory.createConnection();
        // 3. 打开连接
        connection.start();
        // 4. 创建会话
        //第一个参数: 是否开启事务
        //第二个参数: 消息是否自动确认
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        // 5.创建Topic主题模式
        Topic topic = session.createTopic("hello3");
        // 6.创建生产者
        MessageProducer producer = session.createProducer(topic);
        // 7.发送信息
        TextMessage message = session.createTextMessage("234");
        producer.send(message);
        //8.关闭资源
        session.commit();
        producer.close();
        session.close();
        connection.close();
        System.out.println("消息生成成功");

    }
}
