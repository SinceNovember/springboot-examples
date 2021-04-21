package com.simple.activemq.boot.mq.queue;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;

@Component
@EnableScheduling
public class QueueProducer {
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
    private Queue queue;

    @Scheduled(fixedDelay = 3000) //3秒执行1此
    public void send() {
        try {
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("info", "小老弟");
            mapMessage.setString("info1","小表弟");
            //像队列中发送数据
            jmsMessagingTemplate.convertAndSend(queue, mapMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
