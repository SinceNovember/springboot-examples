package com.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeignDemoApplication {

    public static void main(String[] args) {
        // 应用启动
        SpringApplication.run(FeignDemoApplication.class, args);
    }

}