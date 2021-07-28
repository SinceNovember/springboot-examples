package com.simple.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession // 自动化配置 Spring Session 使用 Redis 作为数据源
public class SessionConfiguration {
    /**
     * 自定义session的key名为JESSIONID
     * @return
     */
    @Bean
    public CookieHttpSessionIdResolver sessionIdResolver() {
        // 创建 CookieHttpSessionIdResolver 对象
        CookieHttpSessionIdResolver sessionIdResolver = new CookieHttpSessionIdResolver();

        // 创建 DefaultCookieSerializer 对象
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        sessionIdResolver.setCookieSerializer(cookieSerializer); // 设置到 sessionIdResolver 中
        cookieSerializer.setCookieName("JSESSIONID");

        return sessionIdResolver;
    }
}