package com.simple.redissson.service;


import com.simple.redissson.cacheobject.UserCacheObject;
import com.simple.redissson.dao.redis.UserCacheDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserCacheDao userCacheDao;

    public UserCacheObject get(Integer id) {
        return userCacheDao.get(id);
    }

    public void set(Integer id, UserCacheObject object) {
        userCacheDao.set(id, object);
    }


}
