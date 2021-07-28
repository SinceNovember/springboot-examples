package com.simple.redisson.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

//@Configuration
public class User2Config {
    @Bean
    @ConditionalOnMissingBean(name = "user1")
    public User user7777(){
        return new User2();
    }

}
