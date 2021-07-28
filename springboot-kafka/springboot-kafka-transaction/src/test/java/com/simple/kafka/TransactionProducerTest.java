package com.simple.kafka;

import com.simple.kafka.producer.TransactionProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransactionProducerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionProducer producer;

    @Test
    public void testSyncSendInTransaction() throws ExecutionException, InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.syncSendInTransaction(id, new Runnable() {

            @Override
            public void run() {
                logger.info("[run][我要开始睡觉了]");
                try {
                    Thread.sleep(10 * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("[run][我睡醒了]");
            }

        });

        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
    }
}