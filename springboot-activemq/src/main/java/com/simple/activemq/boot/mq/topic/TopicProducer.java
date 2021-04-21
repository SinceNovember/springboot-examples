package com.simple.activemq.boot.mq.topic;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.Topic;

@Component
@EnableScheduling
public class TopicProducer {
    /*
     * @Resource // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
     * private JmsMessagingTemplate jmsTemplate; //
     * 发送消息，destination是发送到的队列，message是待发送的消息
     *
     * @Scheduled(fixedDelay=3000)//每3s执行1次
     * public void sendMessage(Destination destination, final String message){
     *   jmsTemplate.convertAndSend(destination, message);
     * }
     */

    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private Topic topic;

    @Scheduled(fixedDelay = 3000) //3秒执行1此
    public void send() {
        try {
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("info", "小老弟");
            mapMessage.setString("info1","小表弟");

            //向topic中发送消息
            jmsMessagingTemplate.convertAndSend(topic, mapMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
