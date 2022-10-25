package com.simple.kafka;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * .
 *
 * @author SinceNovember
 * @date 2022/10/24
 */
@SpringBootApplication
@EsMapperScan("com.simple.kafka.mapper.es")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
