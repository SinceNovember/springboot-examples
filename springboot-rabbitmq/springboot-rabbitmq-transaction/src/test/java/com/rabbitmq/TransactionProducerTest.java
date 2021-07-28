package com.rabbitmq;

import com.event.Application;
import com.event.service.TransactionProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransactionProducerTest {

    @Autowired
    private TransactionProducer producer;

    @Test
    public void testSyncSend() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.syncSend(id);

        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
    }

}