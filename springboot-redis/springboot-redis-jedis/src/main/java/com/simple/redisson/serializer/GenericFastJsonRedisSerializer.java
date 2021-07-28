package com.simple.redisson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 自己实现redis的序列化
 */
public class GenericFastJsonRedisSerializer implements RedisSerializer<Object> {
    private final static ParserConfig defaultRedisConfig = new ParserConfig();
    static { defaultRedisConfig.setAutoTypeSupport(true);}


    @Override
    public byte[] serialize(Object object) throws SerializationException {
        System.out.println("开始序列化");
        if (object == null) {
            return new byte[0];
        }
        try{
            return JSON.toJSONBytes(object, SerializerFeature.WriteClassName);
        }
        catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        System.out.println("开始反序列化");
        // 如果为空，则返回空对象
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            // 使用 JSON 解析成对象。
            return JSON.parseObject(new String(bytes, IOUtils.UTF8), Object.class, defaultRedisConfig);
        } catch (Exception ex) {
            throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
        }
    }
}
