package com.simple.redissson.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean(name = "user3")
    public User user3(){
        return new User1();
    }

//    @Bean(name = "user2")
//    public User user2(){
//        return new User2();
//    }
}
