package com.simple.clickhouse.demo.controller;

import com.simple.clickhouse.demo.pojo.UserInfo;
import com.simple.clickhouse.demo.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/userinfo", produces = "application/json")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @GetMapping(value = "/{id}")
    public UserInfo getUserInfoById(@PathVariable("id") Integer id) {
        return userInfoService.getUserById(id);
    }

    @GetMapping
    public List<UserInfo> listUserInfo() {
        return userInfoService.listUser();
    }
}
