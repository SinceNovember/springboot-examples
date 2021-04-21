package com.simple.activemq.original.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


/**
 * 监听器消费消息
 * 重复测试生成和消费的过程。实现一边生成，一边消费的系统
 */
public class QueueMonitorConsumer {
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
        // 5.创建队列
        Queue queue = session.createQueue("hello3");
        //主题使用topic即可
        //Topic queue = session.createTopic("hello3");
        // 6.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        messageConsumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //我们不让程序结束,因为如果结束, 监听就结束了
        while (true){
            //目的: 不让程序死掉
        }
    }
}
