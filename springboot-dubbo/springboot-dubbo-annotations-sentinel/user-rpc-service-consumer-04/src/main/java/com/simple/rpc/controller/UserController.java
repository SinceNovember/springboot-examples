package com.simple.rpc.controller;

import com.simple.rpc.api.UserRpcService;
import com.simple.rpc.dto.UserDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(version = "${dubbo.consumer.UserRpcService.version}", group="user01")
    private UserRpcService userRpcService;

    @GetMapping("/get")
    public UserDTO get(@RequestParam("id") Integer id) {
        return userRpcService.get(id);
    }

}