package com.simple.lock.aop.aspect;

import com.simple.lock.aop.annotations.RedisLock;
import com.simple.lock.aop.config.DistributedLockAutoConfiguration;
import com.simple.lock.aop.lock.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
@ConditionalOnClass(DistributedLock.class)
@AutoConfigureAfter(DistributedLockAutoConfiguration.class)
public class DistributedLockAspect {

    private final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Autowired
    private DistributedLock distributedLock;

    @Pointcut("@annotation(com.simple.lock.aop.annotations.RedisLock)")
    private void lockPoint(){

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String key = redisLock.value();
        if(StringUtils.isEmpty(key)){
            Object[] args = pjp.getArgs();
            key = Arrays.toString(args);
        }
        //获取注解加锁重试次数
        int retryTimes = redisLock.action().equals(RedisLock.LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
        boolean lock = distributedLock.lock(key, redisLock.keepMills(), retryTimes, redisLock.sleepMills());

        //得到锁,执行方法，释放锁
        logger.info("get lock success : " + key);
        try {
            return pjp.proceed();
        } catch (Exception e) {
            logger.info("execute locked method occured an exception", e);
        } finally {
            boolean releaseResult = distributedLock.releaseLock(key);
            logger.info("release lock : " + key + (releaseResult ? " success" : " failed"));
        }
        return null;
    }

}
