package com.simple.shiro.controller;

import com.simple.shiro.constant.Constant;
import com.simple.shiro.entity.SysLoginForm;
import com.simple.shiro.entity.SysUserEntity;
import com.simple.shiro.service.SysUserService;
import com.simple.shiro.service.SysUserTokenService;
import com.simple.shiro.util.R;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class SysLoginController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;


    /**
     * 登录
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(@RequestBody SysLoginForm form) {

           //通过login方法登陆，后续调用自定义Realm的doGetAuthenticationInfo自行判断密码是否正确
//        Subject subject = SecurityUtils.getSubject();
//        subject.login(new UsernamePasswordToken(form.getUsername(), form.getPassword()));
        // 获得指定用户名的 SysUserEntity
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
        if (user == null || !user.getPassword().equals(form.getPassword())) { // 账号不存在、密码错误
            return R.error("账号或密码不正确");
        }
        if (user.getStatus() == 0) { // 账号锁定
            return R.error("账号已被锁定,请联系管理员");
        }

        // 生成 Token ，并返回结果
        return sysUserTokenService.createToken(user.getUserId());
    }

    @RequiresRoles("NORMAL")
    @GetMapping("/test")
    public String test(){
        System.out.println("123");
        return "123";
    }

    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public R logout() {
        sysUserTokenService.logout(getUserId());
        return R.ok();
    }

}