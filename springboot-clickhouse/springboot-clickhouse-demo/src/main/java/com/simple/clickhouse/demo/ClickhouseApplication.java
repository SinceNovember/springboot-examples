package com.simple.clickhouse.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = "com.simple.clickhouse.demo.mapper")
public class ClickhouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClickhouseApplication.class, args);
    }
}
