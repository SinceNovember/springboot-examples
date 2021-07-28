package com.simple.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CallbackController {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.access-token-uri}")
    private String accessTokenUri;


    @GetMapping("/callback")
    public OAuth2AccessToken login(@RequestParam("code") String code) {
        // 创建 AuthorizationCodeResourceDetails 对象
        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        // 创建 OAuth2RestTemplate 对象
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setAuthorizationCode(code); // 设置 code
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setPreservedState("http://127.0.0.1:9090/callback"); // 通过这个方式，设置 redirect_uri 参数
        restTemplate.setAccessTokenProvider(new AuthorizationCodeAccessTokenProvider());
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

}