package com.simple.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@SpringBootApplication
public class Application{

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        logger.debug("just do it");

        SpringApplication.run(Application.class, args);
    }

}