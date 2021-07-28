package com.simple.oauth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @RequestMapping("/hello")
    public String hello() {
        return "world";
    }

}