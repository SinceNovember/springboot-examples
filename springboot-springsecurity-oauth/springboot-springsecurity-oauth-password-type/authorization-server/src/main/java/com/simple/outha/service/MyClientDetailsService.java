package com.simple.outha.service;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Arrays;

/**
 * 自定义与输入的账号密码进行对比的账号信息
 */
public class MyClientDetailsService implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        //可以通过数据库查询的方式来获取客户端信息
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("clientId");
        clientDetails.setClientSecret("11223344");
        clientDetails.setScope(Arrays.asList("read_userinfo", "read_contacts"));
        return clientDetails;
    }
}
