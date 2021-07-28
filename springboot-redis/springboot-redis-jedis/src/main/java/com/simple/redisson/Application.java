package com.simple.redisson;

import com.simple.redisson.test.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application  implements CommandLineRunner {

    @Qualifier("user3")
    @Autowired
    private User user;


    public static void main(String[] args) {
        String[] argList = SpringApplication.run(Application.class, args).getBeanDefinitionNames();
        System.out.println(argList);
    }

    //测试方法
    @Override
    public void run(String... args) throws Exception {
        user.test();
    }
}
