package com.simple.kafka;

import com.simple.kafka.producer.OrderlyProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderlyProducerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderlyProducer producer;

    @Test
    public void testSyncSendOrderly() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            int id = 1;
            SendResult result = producer.syncSendOrderly(id);
            logger.info("[testSyncSend][发送编号：[{}] 发送队列：[{}]]", id, result.getRecordMetadata().partition());
        }

        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
    }

}