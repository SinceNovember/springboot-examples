package com.simple.lock.simple;

import com.simple.lock.simple.lock.RedisDistributeLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleLockTest {

    @Autowired
    private RedisDistributeLock redisDistributeLock;

    @Test
    public void contextLoads() {
        redisDistributeLock.lock("locktest");
        redisDistributeLock.releaseLock("locktest");
    }
}