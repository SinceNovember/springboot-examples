package com.simple.security.service.impl;

import com.simple.security.common.constant.Constants;
import com.simple.security.domain.LoginUser;
import com.simple.security.exception.CaptchaException;
import com.simple.security.exception.CaptchaExpireException;
import com.simple.security.exception.CustomException;
import com.simple.security.exception.UserPasswordNotMatchException;
import com.simple.security.manager.AsyncManager;
import com.simple.security.manager.factory.AsyncFactory;
import com.simple.security.util.MessageUtils;
import com.simple.security.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysLoginService {

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    public String login(String username, String password, String code, String uuid) {
        // <1> 验证图片验证码的正确性
//        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid; // uuid 的作用，是获得对应的图片验证码
//        String captcha = redisCache.getCacheObject(verifyKey);
//        redisCache.deleteObject(verifyKey);
//        if (captcha == null) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//            throw new CaptchaExpireException();
//        }
//        if (!code.equalsIgnoreCase(captcha)) { // 图片验证码不正确
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//            throw new CaptchaException();
//        }
        // 用户验证
        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            // 发生异常，说明验证不通过，记录相应的登陆失败日志
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        //默认的安全上下文中是匿名用户所以可以跳转到/login
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        // 验证通过，记录相应的登陆成功日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        // 生成 Token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }
}
