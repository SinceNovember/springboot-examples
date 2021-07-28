package com.simple.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.shiro.entity.SysRoleEntity;
import com.simple.shiro.util.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveRole(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);


    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);
}