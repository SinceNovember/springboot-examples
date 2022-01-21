package com.simple.canal.sync.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.google.common.collect.Sets;
import com.alibaba.otter.canal.protocol.FlatMessage;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCanalMQ2RedisService<T>  implements CanalSynService<T>{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private Class<T> classCache;

    /**
     * 获取Model名称
     * @return
     */
//    protected abstract String getModelName();

    @Override
    public void process(FlatMessage flatMessage) {

        if (flatMessage.getIsDdl()) {
            ddl(flatMessage);
            return;
        }

        Set<T> data = getData(flatMessage);
        if ("INSERT".equals(flatMessage.getType())) {
            insert(data);
        }

        if ("UPDATE".equals(flatMessage.getType())) {
            update(data);
        }

        if ("DELETE".equals(flatMessage.getType())) {
            delete(data);
        }

    }

    @Override
    public void ddl(FlatMessage flatMessage) {
        //TODO : DDL需要同步，删库清空，更新字段处理

    }

    @Override
    public void insert(Collection<T> list) {
        insertOrUpdate(list);
    }

    @Override
    public void update(Collection<T> list) {
        insertOrUpdate(list);
    }

    @Override
    public void delete(Collection<T> list) {
        Set<String> keys = list.stream().map(this::getWrapRedisKey).collect(Collectors.toSet());
        redisTemplate.delete(keys);
    }

    private void insertOrUpdate(Collection<T> list) {
        redisTemplate.executePipelined((RedisConnection redisConnection) -> {
            for (T data : list) {
                String key = getWrapRedisKey(data);
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                redisConnection.set(keySerializer.serialize(key), valueSerializer.serialize(data));
            }
            return null;
        });
    }

    /**
     * 封装redis的key
     *
     * @param t 原对象
     * @return key
     */
    protected String getWrapRedisKey(T t) {
//        return new StringBuilder()
//                .append(ApplicationContextHolder.getApplicationName())
//                .append(":")
//                .append(getModelName())
//                .append(":")
//                .append(getIdValue(t))
//                .toString();

        throw new IllegalStateException(
                "基类 方法 'getWrapRedisKey' 尚未实现!");
    }


    /**
     * 获取类泛型
     *
     * @return 泛型Class
     */
    protected Class<T> getTypeArguement() {
        if (classCache == null) {
            classCache = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return classCache;
    }

    /**
     * 获取Object标有@Id注解的字段值
     *
     * @param t 对象
     * @return id值
     */
    protected Object getIdValue(T t) {
        Field fieldOfId = getIdField();
        ReflectionUtils.makeAccessible(fieldOfId);
        return ReflectionUtils.getField(fieldOfId, t);
    }

    /**
     * 获取Class标有@Id注解的字段名称
     *
     * @return id字段名称
     */
    protected Field getIdField(){

        Class<T> clz = getTypeArguement();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            Id annotation = field.getAnnotation(Id.class);

            if (annotation != null) {
                return field;
            }
        }
        throw new IllegalArgumentException();

    }

    /**
     * 转换Canal的FlatMessage中data成泛型对象
     *
     * @param flatMessage Canal发送MQ信息
     * @return 泛型对象集合
     */
    protected Set<T> getData(FlatMessage flatMessage) {
        List<Map<String, String>> sourceData = flatMessage.getData();
        Set<T> targetData = Sets.newHashSetWithExpectedSize(sourceData.size());
        for (Map<String, String> map : sourceData) {
            T t = JSON.parseObject(JSON.toJSONString(map), getTypeArguement());
                targetData.add(t);
        }
        return targetData;
    }
}
