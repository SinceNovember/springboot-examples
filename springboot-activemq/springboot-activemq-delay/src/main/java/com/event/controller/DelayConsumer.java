package com.event.controller;

import com.event.event.Demo02Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class DelayConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = Demo02Message.QUEUE)
    public void onMessage(Demo02Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}
