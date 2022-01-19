package com.simple.lock.simple.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RedisDistributeLock extends AbstractDistributeLock{

    private final Logger logger = LoggerFactory.getLogger(RedisDistributeLock.class);

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private ThreadLocal<String> lockFlag = new ThreadLocal<String>();

    public static  String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedis(key, expire);
        while ((!result) && retryTimes-- > 0) {
            try {
                logger.debug("lock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setRedis(key, expire);

        }
        return result;
    }

    private boolean setRedis(String key, long expire) {
        try{
            String result = redisTemplate.execute((RedisCallback<String>) con -> {
                JedisCommands commands = (JedisCommands) con.getNativeConnection();
                String uuid = UUID.randomUUID().toString();
                lockFlag.set(uuid);
                return commands.set(key, uuid, new SetParams().nx().px(expire));
            });
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            logger.error("set redis occured an exception", e);
        }
        return false;
    }

    /**
     * 释放锁
     * @param key
     * @return
     */
    @Override
    public boolean releaseLock(String key) {
        try{
            List<String> keys = new ArrayList<String>();
            keys.add(key);
            List<String> args = new ArrayList<String>();
            args.add(lockFlag.get());
            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long result = redisTemplate.execute((RedisCallback<Long>) con -> {
                Object nativeConnection = con.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                } // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                return 0L;
            });
            return result != null && result > 0;
        } catch (Exception e) {
            logger.error("release lock occured an exception", e);
        }
        return false;
    }
}
