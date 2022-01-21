package com.simple.canal.sync.kafka.deploy;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置redis的key-value
     */
    public void setString(String key, String value) {
        setString(key, value, null);
    }

    /**
     * 设置redis的key-value，带过期时间
     */
    public void setString(String key, String value, Long timeOut) {
        stringRedisTemplate.opsForValue().set(key, value);
        if (timeOut != null) {
            stringRedisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取redis中key对应的值
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除redis中key对应的值
     */
    public Boolean deleteKey(String key) {
        return stringRedisTemplate.delete(key);
    }
}
