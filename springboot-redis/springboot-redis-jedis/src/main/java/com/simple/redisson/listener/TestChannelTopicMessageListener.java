package com.simple.redisson.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis的订阅/推送的消息监听器
 */
public class TestChannelTopicMessageListener implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 接受订阅到的topic对应的消息
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("收到 ChannelTopic 消息：");
        System.out.println("线程编号：" + Thread.currentThread().getName());
        System.out.println("message：" + message);
        System.out.println("pattern：" + new String(pattern));
    }
}
