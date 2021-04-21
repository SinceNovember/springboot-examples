package com.simple.activemq.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class MqSbApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqSbApplication.class, args);
    }
}
