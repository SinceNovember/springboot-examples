package com.simple.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

@Service
public class KafkaProducerService {

    private Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    @Resource
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String data) {
        log.info("kafka sendMessage start");
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("kafka sendMessage error, ex = {}, topic = {}, data = {}", ex, topic, data);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                log.info("kafka sendMessage success topic = {}, data = {}",topic, data);
            }
        });
        log.info("kafka sendMessage end");
    }
}
