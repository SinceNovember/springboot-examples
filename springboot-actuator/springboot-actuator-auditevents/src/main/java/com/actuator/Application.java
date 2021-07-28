package com.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 访问 http://127.0.0.1:8080/actuator/auditevents
 * 用于获取应用的最近的审计事件( AuditEvents )
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}