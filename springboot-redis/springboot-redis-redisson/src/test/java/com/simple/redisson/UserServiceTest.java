package com.simple.redisson;

import com.simple.redissson.Application;
import com.simple.redissson.cacheobject.UserCacheObject;
import com.simple.redissson.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSet() {
        UserCacheObject object = new UserCacheObject()
                .setId(9)
                .setName("芋道源码")
                .setGender(1); // 男
        userService.set(object.getId(), object);
    }

    @Test
    public void testGet(){
        System.out.println(userService.get(9));
    }

}