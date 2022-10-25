package com.simple.clickhouse.demo.controller;

import com.simple.clickhouse.demo.pojo.GroupInfo;
import com.simple.clickhouse.demo.pojo.UserInfo;
import com.simple.clickhouse.demo.service.GroupInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/groupinfo", produces = "application/json")
public class GroupInfoController {

    @Resource
    private GroupInfoService groupInfoService;

    @GetMapping(value = "/{id}")
    public GroupInfo getGroupInfoById(@PathVariable("id") Integer id) {
        return groupInfoService.getGroupInfoById(id);
    }

}
