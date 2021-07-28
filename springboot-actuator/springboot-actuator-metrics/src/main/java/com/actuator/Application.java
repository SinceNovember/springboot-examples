package com.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 访问 http://127.0.0.1:8080/actuator/metrics
 * http://127.0.0.1:8080/actuator/mappings SpringMVC mapping信息
 * http://127.0.0.1:8080/actuator/loggers 日志信息
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}