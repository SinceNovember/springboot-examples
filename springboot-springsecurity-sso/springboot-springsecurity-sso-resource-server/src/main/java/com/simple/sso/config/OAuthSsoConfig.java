package com.simple.sso.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableOAuth2Sso // 开启 Sso 功能
public class OAuthSsoConfig {

}