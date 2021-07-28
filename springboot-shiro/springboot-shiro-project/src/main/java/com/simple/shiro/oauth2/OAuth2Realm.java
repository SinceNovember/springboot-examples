package com.simple.shiro.oauth2;

import com.simple.shiro.entity.SysUserEntity;
import com.simple.shiro.entity.SysUserTokenEntity;
import com.simple.shiro.service.ShiroService;
import com.simple.shiro.service.SysRoleService;
import com.simple.shiro.service.SysUserRoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 * 调用Subject.login方法是，最终最授权、认证的方法
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysRoleService roleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获得 SysUserEntity 对象
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        // 用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);
        List<Long> roleList = userRoleService.queryRoleIdList(userId);
        String roleName = roleService.getById(roleList.get(0)).getRoleName();

        // 创建 SimpleAuthorizationInfo 对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        HashSet<String> set = new HashSet();
        set.add(roleName);
        info.setRoles(set);
        return info;
    }

    /**
     * 认证(登录时调用)
     * AuthenticationToken token:Subject.login()中传入的参数
     * 在AuthenticatingRealm中进行token已经返回的AuthenticationInfo结果进行匹配密码
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String accessToken = (String) token.getPrincipal();
        Object accessSecret =  token.getCredentials();
        // 根据 accessToken ，查询用户信息
        SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        // token 失效
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //  查询用户信息
        SysUserEntity user = shiroService.queryUser(tokenEntity.getUserId());
        // 账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 创建 SimpleAuthenticationInfo 对象
        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }

}