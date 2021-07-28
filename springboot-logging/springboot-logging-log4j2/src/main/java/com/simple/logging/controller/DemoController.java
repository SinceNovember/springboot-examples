package com.simple.logging.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  使用 Postman ，请求 POST http://127.0.0.1:8080/actuator/loggers/com.simple.logging.controller/ 地址
 *  发送 {
 *      "configuredLevel" : "INFO"
 *  }
 *  ，修改该 Logger 的日志级别
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/debug")
    public void debug() {
        logger.debug("debug");
    }

    @GetMapping("/info")
    public void info() {
        logger.info("info");
    }

    @GetMapping("/error")
    public void error() {
        logger.error("error");
    }

}