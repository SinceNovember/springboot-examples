package com.simple.redisson;

import com.simple.redisson.cacheobject.UserCacheObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StringTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStringRedis() {
        stringRedisTemplate.opsForValue().set("yunai", "shuai");
    }

    @Test
    public void testStringSetKey02() {
        redisTemplate.opsForValue().set("yunai", "shuai");
    }

    @Test
    public void testSetAdd(){
        stringRedisTemplate.opsForSet().add("yunai_descriptions_1", "shuai", "cai");
    }

    /**
     * 序列化对象值
     */
    @Test
    public void testStringSetKeyUserCache() {
        UserCacheObject object = new UserCacheObject()
                .setId(4)
                .setName("芋道源码4")
                .setGender(1); // 男
        String key = String.format("user:%d", object.getId());
        redisTemplate.opsForValue().set(key, object);
    }

    /**
     * 反序列化值
     */
    @Test
    public void testStringGetKeyUserCache() {
        String key = String.format("user:%d", 1);
        Object value = redisTemplate.opsForValue().get(key);
        System.out.println(value);
    }



}
