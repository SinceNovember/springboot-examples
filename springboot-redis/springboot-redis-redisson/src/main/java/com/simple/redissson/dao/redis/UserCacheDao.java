package com.simple.redissson.dao.redis;


import com.simple.redissson.cacheobject.UserCacheObject;
import com.simple.redissson.util.JSONUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Redis的Dao,替代数据库的Dao
 */
@Repository
public class UserCacheDao {

    public static final String KEY_PATTERN = "user:%d"; //user:用户编号

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    private RedisTemplate redisTemplate;
    private ValueOperations<String, String> operations;

    private static String buildKey(Integer id) {
        return String.format(KEY_PATTERN, id);
    }

    public UserCacheObject get(Integer id) {
        String key = buildKey(id);
        String value = operations.get(key);
        return JSONUtil.parseObject(value, UserCacheObject.class);
    }

    public void set(Integer id, UserCacheObject object) {
        String key = buildKey(id);
        String value = JSONUtil.toJSONString(object);
        operations.set(key, value);
    }
}
