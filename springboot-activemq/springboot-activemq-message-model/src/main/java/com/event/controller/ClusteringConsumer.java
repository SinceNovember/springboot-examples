package com.event.controller;

import com.event.config.ActiveMQConfig;
import com.event.event.ClusteringMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ClusteringConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = ClusteringMessage.QUEUE,
            containerFactory = ActiveMQConfig.CLUSTERING_JMS_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public void onMessage(ClusteringMessage message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
