package com.simple.custom.argreturnhandler.controller;

import com.simple.custom.argreturnhandler.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/say")
    public User sayFrom(@RequestBody User user) {
        System.out.println(user.getUsername());
        return user;
    }
    @PostMapping("/say1")
    public String sayFrom1(@RequestBody String user) {
        System.out.println(user);
        return "user";
    }
}
