package com.simple.lock.aop;

import com.simple.lock.aop.annotations.RedisLock;
import com.simple.lock.aop.lock.RedisDistributeLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AopLockTest {

    @Autowired
    private RedisDistributeLock redisDistributeLock;

    @Test
    @RedisLock
    public void contextLoads() {
        System.out.println("执行锁");
    }
}