package com.simple.shiro.service.impl;

import com.simple.shiro.constant.Constant;
import com.simple.shiro.dao.SysMenuDao;
import com.simple.shiro.dao.SysUserDao;
import com.simple.shiro.dao.SysUserTokenDao;
import com.simple.shiro.entity.SysMenuEntity;
import com.simple.shiro.entity.SysUserEntity;
import com.simple.shiro.entity.SysUserTokenEntity;
import com.simple.shiro.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;
        // 系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            // 如果是管理员，则查询所有 SysMenuEntity 数组
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            // 如果是普通用户，则查询其拥有的 SysMenuEntity 数组
            permsList = sysUserDao.queryAllPerms(userId);
        }
        // 用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            // 使用逗号分隔，每一个 perms
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }
}