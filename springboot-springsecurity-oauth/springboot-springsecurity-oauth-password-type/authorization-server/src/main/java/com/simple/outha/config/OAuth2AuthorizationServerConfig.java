package com.simple.outha.config;

import com.simple.outha.service.MyClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * 授权服务器配置
 *  http://localhost:8080/oauth/token
 *  访问授权服务器 带上Authorization 的type为Basic Auth 输入clientId以及securityId 以及Body携带grant_type为password 输入username以及password
 *  http://localhost:8080/oauth/check_token
 *  携带上token即可获取token的用户信息
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
        oauthServer.checkTokenAccess("isAuthenticated()");
//        oauthServer.tokenKeyAccess("isAuthenticated()")
//                .checkTokenAccess("isAuthenticated()");
//        oauthServer.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new MyClientDetailsService());
//             clients.withClient("clientapp").secret("112233")// Client 账号、密码。
//                .authorizedGrantTypes("password") // 密码模式
//                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
//                .and().withClient() // 可以继续配置新的 Client
        ;

    }
}
