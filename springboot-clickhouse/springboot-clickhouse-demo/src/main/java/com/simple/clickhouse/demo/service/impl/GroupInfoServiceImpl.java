package com.simple.clickhouse.demo.service.impl;

import com.simple.clickhouse.demo.mapper.mysql.GroupInfoMapper;
import com.simple.clickhouse.demo.pojo.GroupInfo;
import com.simple.clickhouse.demo.service.GroupInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GroupInfoServiceImpl implements GroupInfoService {

    @Resource
    private GroupInfoMapper groupInfoMapper;

    @Override
    public GroupInfo getGroupInfoById(Integer id) {
        return groupInfoMapper.findById(id);
    }
}
