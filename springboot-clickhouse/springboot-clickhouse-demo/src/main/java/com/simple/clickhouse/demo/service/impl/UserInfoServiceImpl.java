package com.simple.clickhouse.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.clickhouse.demo.mapper.clickhouse.UserInfoMapper;
import com.simple.clickhouse.demo.pojo.UserInfo;
import com.simple.clickhouse.demo.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> listUser() {
        return userInfoMapper.findList();
    }

    @Override
    public UserInfo getUserById(Integer id) {
        return userInfoMapper.findById(id);
    }

    @Override
    public int addUser(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }
}
