package com.simple.redisson;

import com.simple.redissson.Application;
import com.simple.redissson.service.RedissonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedissonTest {

    private static final Logger log = LoggerFactory.getLogger(RedissonTest.class);

    @Autowired
    private RedissonService redissonService;

    @Test
    public void test(){
        RLock lock = redissonService.getRLock("rlock");
        try {
//            lock.lock();
            // TODO: 2020/5/15 第一个参数为 30， 尝试获取锁的的最大等待时间为30s
            // TODO: 2020/5/15 第二个参数为 60，  上锁成功后60s后锁自动失效
            // 尝试获取锁（可重入锁,不会造成死锁）
            boolean lockFlag = lock.tryLock(30, 60, TimeUnit.SECONDS);
            if(lockFlag){
                // 业务代码
                log.info("进入业务代码: rlock");
            } else {
                log.error("当前锁资源被占用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<未获取到锁");
            }

        } catch (Exception e) {
            log.error("", e);
        } finally{
            lock.unlock();
        }
    }
}
