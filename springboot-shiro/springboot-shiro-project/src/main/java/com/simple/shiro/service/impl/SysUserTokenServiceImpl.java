package com.simple.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.shiro.dao.SysUserTokenDao;
import com.simple.shiro.entity.SysUserTokenEntity;
import com.simple.shiro.service.SysUserTokenService;
import com.simple.shiro.util.R;
import com.simple.shiro.util.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {

    // 12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public R createToken(long userId) {
        // 生成一个 token
        String token = TokenGenerator.generateValue();

        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        // 判断是否生成过 token
        SysUserTokenEntity tokenEntity = this.getById(userId);
        if (tokenEntity == null) { // 新增 SysUserTokenEntity
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            // 保存 token
            this.save(tokenEntity);
        } else { // 更新 SysUserTokenEntity
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            // 更新 token
            this.updateById(tokenEntity);
        }

        // 返回 token 和过期时间
        return R.ok().put("token", token).put("expire", EXPIRE);
    }

    @Override
    public void logout(long userId) {
        // 生成一个token
        String token = TokenGenerator.generateValue();

        // 修改token
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        this.updateById(tokenEntity);
    }
}
