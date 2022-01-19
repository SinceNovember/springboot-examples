package com.simple.sentinel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    // 使用SpringBoot封装的RestTemplate对象
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/get")
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value;
    }

    @RequestMapping("/set")
    public String set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return "success";
    }
}