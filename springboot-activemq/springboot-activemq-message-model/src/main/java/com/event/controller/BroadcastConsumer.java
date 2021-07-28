package com.event.controller;

import com.event.config.ActiveMQConfig;
import com.event.event.BroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BroadcastConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = BroadcastMessage.TOPIC,
            containerFactory = ActiveMQConfig.BROADCAST_JMS_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public void onMessage(BroadcastMessage message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}