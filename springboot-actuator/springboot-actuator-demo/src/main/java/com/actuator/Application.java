package com.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 访问 http://127.0.0.1:8080/actuator/health
 * http://127.0.0.1:8080/actuator/beans
 * http://127.0.0.1:8080/actuator/conditions
 * http://127.0.0.1:8080/actuator/env
 * http://127.0.0.1:8080/actuator/configprops
 * http://127.0.0.1:8080/actuator/heapdump  获取应用的 JVM Heap Dump
 * POST 请求 http://127.0.0.1:8080/actuator/shutdown 地址 进行应用的优雅关闭 配置 management.endpoint.shutdown.enabled = true 来进行开启
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}