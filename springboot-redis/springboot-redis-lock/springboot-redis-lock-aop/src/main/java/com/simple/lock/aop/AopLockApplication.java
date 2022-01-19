package com.simple.lock.aop;

import com.simple.lock.aop.annotations.RedisLock;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.tools.jar.CommandLine;

@SpringBootApplication
public class AopLockApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AopLockApplication.class, args);
    }


    @Override
    @RedisLock
    public void run(String... args) throws Exception {
        System.out.println("test");
    }
}
