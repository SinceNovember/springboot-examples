package com.hystrix.config;

import com.hystrix.feign.UserServiceFeignClient;
import com.hystrix.feign.UserServiceFeignClient02;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.spring.SpringContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public UserServiceFeignClient userServiceFeignClient() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(UserServiceFeignClient.class, "http://127.0.0.1:8082"); // 目标地址
    }

    /**
     * 使用SpringMVC Contract契约
     * @return
     */
    @Bean
    public UserServiceFeignClient02 userServiceFeignClient02() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringContract())
                .target(UserServiceFeignClient02.class, "http://127.0.0.1:8082"); // 目标地址
    }

}