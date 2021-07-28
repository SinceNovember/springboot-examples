package com.simple.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * 授权服务器配置
 * 授权地址:
 * http://127.0.0.1:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://127.0.0.1:9090/callback&response_type=code&scope=read_userinfo
 * 授权后将返回授权码
 * 携带授权码通过
 *grant_type:authorization_code
 * code:登陆后回调地址返回的授权码
 * redirect_uri:授权回调地址
 * 获得访问令牌
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 用户认证 Manager
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("isAuthenticated()")
//            .tokenKeyAccess("permitAll()")
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("clientapp").secret("112233") // Client 账号、密码。
                .authorizedGrantTypes("authorization_code") // 授权码模式
                .redirectUris("http://127.0.0.1:9090/callback") // 配置回调地址，选填。
                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
//                .and().withClient() // 可以继续配置新的 Client
        ;
    }

}