package com.simple.security.handler;

import com.alibaba.fastjson.JSON;
import com.simple.security.common.constant.Constants;
import com.simple.security.common.constant.HttpStatus;
import com.simple.security.domain.AjaxResult;
import com.simple.security.domain.LoginUser;
import com.simple.security.manager.AsyncManager;
import com.simple.security.manager.factory.AsyncFactory;
import com.simple.security.service.impl.TokenService;
import com.simple.security.util.ServletUtils;
import com.simple.security.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 自定义退出处理类 返回成功
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // <1> 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 如果有登录的情况下
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // <2> 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // <3> 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        // <4> 响应退出成功
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }

}
