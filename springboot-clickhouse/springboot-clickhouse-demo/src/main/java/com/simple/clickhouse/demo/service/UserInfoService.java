package com.simple.clickhouse.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.clickhouse.demo.pojo.UserInfo;

import java.util.List;

public interface UserInfoService extends IService<UserInfo> {

    List<UserInfo> listUser();

    UserInfo getUserById(Integer id);

    int addUser(UserInfo userInfo);
}
