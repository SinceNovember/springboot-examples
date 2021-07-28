package com.rabbitmq;

import com.event.Application;
import com.event.service.BatchProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BatchProducerTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BatchProducer producer;

    /**
     * 需要等待到超时
     * @throws InterruptedException
     */
    @Test
    public void testSyncSend01() throws InterruptedException {
        // 发送 3 条消息
        this.testSyncSend(3);
    }

    /**
     * 拉到足够多不需要等待
     * @throws InterruptedException
     */
    @Test
    public void testSyncSen02() throws InterruptedException {
        // 发送 10 条消息
        this.testSyncSend(10);
    }

    private void testSyncSend(int n) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            // 同步发送消息
            int id = (int) (System.currentTimeMillis() / 1000);
            producer.syncSend(id);
            logger.info("[testSyncSendMore][发送编号：[{}] 发送成功]", id);
        }

        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
    }


}