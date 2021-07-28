package com.event.service;

import com.event.event.Demo01Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class SimpleProducer{

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void syncSend(Integer id) {
        // 创建 ClusteringMessage 消息
        Demo01Message message = new Demo01Message();
        message.setId(id);
        // 同步发送消息
        jmsTemplate.convertAndSend(Demo01Message.QUEUE, message);
    }

    /**
     * 异步发送
     * 结合 Spring 异步调用
     * @param id
     * @return
     */
    @Async
    public ListenableFuture<Void> asyncSend(Integer id) {
        try {
            // 发送消息
            this.syncSend(id);
            // 返回成功的 Future
            return AsyncResult.forValue(null);
        } catch (Throwable ex) {
            // 返回异常的 Future
            return AsyncResult.forExecutionException(ex);
        }
    }
}