package com.simple.kafka;

import com.simple.kafka.producer.AckProducer;
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
public class AckProducerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AckProducer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        for (int id = 1; id <= 2; id++) {
            SendResult result = producer.syncSend(id);
            logger.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);
        }

        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
    }

}