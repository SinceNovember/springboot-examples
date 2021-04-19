package com.simple.kafka.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simple.kafka.service.KafkaProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SendMsgController {

    @Resource
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public String sendMsg(@RequestBody String body) {
        JSONObject object = JSON.parseObject(body);
        String topicName = object.getString("topicName");
        String data = object.getString("data");
        kafkaProducerService.sendMessage(topicName, data);
        return "send Success";
    }
}
