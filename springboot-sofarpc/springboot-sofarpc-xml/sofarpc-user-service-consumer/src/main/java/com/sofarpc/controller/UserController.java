package com.sofarpc.controller;

import com.sofarpc.api.UserRpcService;
import com.sofarpc.dto.UserAddDTO;
import com.sofarpc.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRpcService userRpcService;

    @GetMapping("/get")
    public UserDTO get(@RequestParam("id") Integer id) {
        return userRpcService.get(id);
    }

    @GetMapping("/add") // 为了方便测试，实际使用 @PostMapping
    public Integer add(@RequestParam("name") String name,
                       @RequestParam("gender") Integer gender) {
        UserAddDTO addDTO = new UserAddDTO().setName(name).setGender(gender);
        return userRpcService.add(addDTO);
    }

}