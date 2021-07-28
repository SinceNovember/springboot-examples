package com.simple.rocketmq.boot.producer;

import com.simple.rocketmq.boot.entity.User;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * 消息生产者
 */
@Service
public class ProducerService {
    private Logger log = LoggerFactory.getLogger(ProducerService.class);

    @Resource
    private RocketMQTemplate mqTemplate;

    @Value(value = "${rocketmq.topic.string}")
    private String springTopic;

    @Value(value = "${rocketmq.topic.user}")
    private String userTopic;

    @Value(value = "${rocketmq.tag}")
    private String tag;

    public SendResult sendString(String message){
        String address = mqTemplate.getProducer().getNamesrvAddr();
        SendResult sendResult = mqTemplate.syncSend(springTopic + ":" + tag, message);
        log.info("syncSend String to topic {} sendResult={} \n", springTopic, sendResult);
        return sendResult;
    }

    public SendResult sendUser(User user) {
        // 发送 User
        SendResult sendResult = mqTemplate.syncSend(userTopic, user,600000);
        log.info("syncSend User to topic {} sendResult= {} \n", userTopic, sendResult);
        return sendResult;
    }


}
