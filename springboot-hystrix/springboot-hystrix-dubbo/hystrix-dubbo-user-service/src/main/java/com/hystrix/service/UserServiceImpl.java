package com.hystrix.service;

import com.hystrix.userservice.UserService;

@org.apache.dubbo.config.annotation.Service(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public String getUser(Integer id) {
        return "User:" + id;
    }

}
