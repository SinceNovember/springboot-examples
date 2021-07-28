package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);
    }

    @Component
    public class ValueCommandLineRunner implements CommandLineRunner {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Value("${application-test}")
        private String applicationTest;
        @Override
        public void run(String... args) {
            logger.info("applicationTest:" + applicationTest);
        }

    }
}
