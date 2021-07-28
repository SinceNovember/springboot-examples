package com.event.service;

import com.event.event.Demo02Message;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时消息
 * ActiveMQ 默认未开启定时消息的功能，需要我们手动去配置开启。通过编辑 conf/activemq.xml 配置文件，添加 schedulerSupport="true" 来开启。示例如下：
 *
 * <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true">
 * 配置完成，通过 bin/macosx/activemq restart 将 ActiveMQ 重启即可生效。
 */
@Component
public class DelayProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void syncSend(Integer id, Integer delay) {
        // 创建 Demo02Message 消息
        Demo02Message message = new Demo02Message();
        message.setId(id);
        // 创建 Header
        Map<String, Object> headers = null;
        if (delay != null && delay > 0) {
            headers = new HashMap<>();
            headers.put(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
        }
        // 同步发送消息
        jmsTemplate.convertAndSend(Demo02Message.QUEUE, message, headers);
    }
}