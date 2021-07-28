package com.springclouddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @RestController
    static class TestController {

        @GetMapping("/echo")
        public String echo(String name) {
            return "provider:" + name;
        }

    }

}
