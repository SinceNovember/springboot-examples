package com.event.controller;

import com.event.event.Demo04Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderlyConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = Demo04Message.QUEUE_0)
    @JmsListener(destination = Demo04Message.QUEUE_1)
    @JmsListener(destination = Demo04Message.QUEUE_2)
    @JmsListener(destination = Demo04Message.QUEUE_3)
    public void onMessage(Demo04Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }


}