package com.simple.shiro.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mchange.v1.util.MapUtils;
import com.simple.shiro.dao.SysUserRoleDao;
import com.simple.shiro.entity.SysUserRoleEntity;
import com.simple.shiro.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.removeByMap(MapUtil.of("user_id", userId));

        if(roleIdList == null || roleIdList.size() == 0){
            return ;
        }

        //保存用户与角色关系
        for(Long roleId : roleIdList){
            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);

            this.save(sysUserRoleEntity);
        }
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    @Override
    public int deleteBatch(Long[] roleIds){
        return baseMapper.deleteBatch(roleIds);
    }
}