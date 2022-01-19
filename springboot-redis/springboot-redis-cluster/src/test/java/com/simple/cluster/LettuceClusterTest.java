package com.simple.cluster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("LettuceCluster")
public class LettuceClusterTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("hahha3", "enjoyitlife2020");
        String name = redisTemplate.opsForValue().get("hahha3").toString();
        System.out.println(name);
    }
}
