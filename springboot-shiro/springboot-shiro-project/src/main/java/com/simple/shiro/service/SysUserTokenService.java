package com.simple.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.shiro.entity.SysUserTokenEntity;
import com.simple.shiro.util.R;

/**
 * 用户Token
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

    /**
     * 生成token
     * @param userId  用户ID
     */
    R createToken(long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(long userId);

}