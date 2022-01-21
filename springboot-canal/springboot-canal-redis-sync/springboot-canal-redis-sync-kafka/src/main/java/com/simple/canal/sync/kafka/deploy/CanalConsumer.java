package com.simple.canal.sync.kafka.deploy;

import com.alibaba.fastjson.JSONObject;
import com.simple.canal.sync.kafka.model.TbCommodityInfo;
import com.simple.canal.sync.kafka.model.CanalBean;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CanalConsumer {

    //日志记录
    private static Logger log = LoggerFactory.getLogger(CanalConsumer.class);

    /**
     * topic名称
     */
    private static final String KAFKA_TOPIC_NAME = "canaltopic";

    //redis操作工具类
    @Resource
    private RedisClient redisClient;


    //监听的队列名称为：canaltopic
    @KafkaListener(topics = KAFKA_TOPIC_NAME)
    public void receive(ConsumerRecord<?, ?> consumer) {
        String value = (String) consumer.value();
        log.info("topic名称:{},key:{},分区位置:{},下标:{},value:{}", consumer.topic(), consumer.key(),consumer.partition(), consumer.offset(), value);
        //转换为javaBean
        CanalBean canalBean = JSONObject.parseObject(value, CanalBean.class);
        //获取是否是DDL语句
        boolean isDdl = canalBean.isDdl();
        //获取类型
        String type = canalBean.getType();
        //不是DDL语句
        if (!isDdl) {
            List<TbCommodityInfo> tbCommodityInfos = canalBean.getData();
            //过期时间
            long TIME_OUT = 600L;
            if ("INSERT".equals(type)) {
                //新增语句
                for (TbCommodityInfo tbCommodityInfo : tbCommodityInfos) {
                    String id = tbCommodityInfo.getId();
                    //新增到redis中,过期时间是10分钟
                    redisClient.setString(id, JSONObject.toJSONString(tbCommodityInfo), TIME_OUT);
                }
            } else if ("UPDATE".equals(type)) {
                //更新语句
                for (TbCommodityInfo tbCommodityInfo : tbCommodityInfos) {
                    String id = tbCommodityInfo.getId();
                    //更新到redis中,过期时间是10分钟
                    redisClient.setString(id, JSONObject.toJSONString(tbCommodityInfo), TIME_OUT);
                }
            } else if ("DELETE".equals(type)) {
                //删除语句
                for (TbCommodityInfo tbCommodityInfo : tbCommodityInfos) {
                    String id = tbCommodityInfo.getId();
                    //从redis中删除
                    redisClient.deleteKey(id);
                }
            }
        }


    }
}
