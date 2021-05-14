package com.simple.custom.argumentresolver.controller;

import com.simple.custom.argumentresolver.annoation.UserId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserController {
    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public void user(@UserId String userId, Integer test) {
        System.out.println(userId);
    }
}
