package com.simple.activemq.original.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * topic类型的消费者
 */
public class TopicConsumer {
    public static void main(String[] args) throws JMSException {
        // 1.创建连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        // 2.创建连接
        Connection connection = connectionFactory.createConnection();
        // 3.开启连接
        connection.start();
        // 4.创建会话
        /*
         *  第一个参数,是否使用事务
         * 如果设置true,操作消息队列后, 必须使用 session.commit();
         * 如果设置false, 操作消息队列后, 不使用 session.commit();
         */
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        // 5.创建topic
        Topic queue = session.createTopic("hello3");
        // 6.创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        while (true) {
            TextMessage message = (TextMessage) consumer.receive(10000);
            if (message!=null) {
                System.out.println(message.getText());
            }else{
                break;
            }
        }
        // 7.关闭连接
        session.commit();
        session.close();
        connection.close();
        System.out.println("消费结束");
    }

}
