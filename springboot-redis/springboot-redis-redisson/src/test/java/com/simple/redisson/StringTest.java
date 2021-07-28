package com.simple.redisson;

import com.simple.redissson.Application;
import com.simple.redissson.cacheobject.UserCacheObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StringTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStringRedis() {
        stringRedisTemplate.opsForValue().set("yunai7789", "shuai");
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
                .setId(99)
                .setName("芋道源码99")
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
