package com.event.controller;

import com.event.event.Demo01Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = Demo01Message.QUEUE)
    public void onMessage(Demo01Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

//    @JmsListener(destination = Demo01Message.QUEUE)
//    public void onMessage(javax.jms.Message message) {
//        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
//    }

}
