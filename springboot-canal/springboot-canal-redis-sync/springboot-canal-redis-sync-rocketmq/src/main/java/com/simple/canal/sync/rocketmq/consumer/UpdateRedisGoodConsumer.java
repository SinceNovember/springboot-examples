package com.simple.canal.sync.rocketmq.consumer;

import com.alibaba.google.common.collect.Sets;
import com.alibaba.otter.canal.protocol.FlatMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
//广播模式
//@RocketMQMessageListener(topic = "seckillgood", consumerGroup = "UpdateRedis", messageModel = MessageModel.BROADCASTING)
//集群模式
@RocketMQMessageListener(topic = "test", consumerGroup = "UpdateRedis")
public class UpdateRedisGoodConsumer extends AbstractCanalMQ2RedisService<SeckillGoodPO> implements RocketMQListener<FlatMessage> {

    private String modelName = "seckillgood";

    /**
     * 封装redis的key
     *
     * @param t 原对象
     * @return keyx
     */
    protected String getWrapRedisKey(SeckillGoodPO t) {
        return new StringBuilder()
//                .append(ApplicationContextHolder.getApplicationName())
                .append("seckill")
                .append(":")
//                .append(getModelName())
                .append("seckillgood")
                .append(":")
                .append(t.getId())
                .toString();

    }

    @Override
    public void onMessage(FlatMessage flatMessage) {
        process(flatMessage);
    }

    /**
     * 转换Canal的FlatMessage中data成泛型对象
     *
     * @param flatMessage Canal发送MQ信息
     * @return 泛型对象集合
     */
    @Override
    protected Set<SeckillGoodPO> getData(FlatMessage flatMessage) {
        if (!"QUERY".equals(flatMessage.getType())) {
            List<Map<String, String>> sourceData = flatMessage.getData();
            Set<SeckillGoodPO> targetData = Sets.newHashSetWithExpectedSize(sourceData.size());
            for (Map<String, String> map : sourceData) {
                SeckillGoodPO po = new SeckillGoodPO();
                po.setId(Long.valueOf(map.get("id")));
                //省略其他的属性
                targetData.add(po);
            }
            return targetData;
        }
        return Collections.emptySet();

    }

}
