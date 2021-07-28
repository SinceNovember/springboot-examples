package com.simple.security.service.impl;

import com.simple.security.domain.LoginUser;
import com.simple.security.domain.SysRole;
import com.simple.security.util.ServletUtils;
import com.simple.security.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 *
 * @author ruoyi
 */
@Service("ss")
public class PermissionService {

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";

    @Autowired
    private TokenService tokenService;

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        // 如果未设置需要的权限，强制不具备。
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 如果不存在，或者没有任何权限，说明权限验证不通过
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        // 判断是否包含权限
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission) {
        return !hasPermi(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions) {
        // 如果未设置需要的权限，强制不具备。
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 如果不存在，或者没有任何权限，说明权限验证不通过
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        // 判断是否包含指定的任一权限
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        // 如果未设置需要的角色，强制不具备。
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 如果不存在，或者没有任何角色，说明权限验证不通过
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        // 判断是否包含指定角色
        for (SysRole sysRole : loginUser.getUser().getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.contains(roleKey) // 超级管理员的特殊处理
                    || roleKey.contains(StringUtils.trim(role))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles) {
        // 如果未设置需要的角色，强制不具备。
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 如果不存在，或者没有任何角色，说明权限验证不通过
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        // 判断是否包含指定的任一角色
        for (String role : roles.split(ROLE_DELIMETER)) {
            if (hasRole(role)) { // 这里实现有点问题，会循环调用 hasRole 方法，重复从 Redis 中读取当前 LoginUser
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }

}
