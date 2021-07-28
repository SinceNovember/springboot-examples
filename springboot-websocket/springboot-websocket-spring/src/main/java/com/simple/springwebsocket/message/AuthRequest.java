package com.simple.springwebsocket.message;

/**
 * 用户认证请求
 */
public class AuthRequest implements Message{
    public static final String TYPE = "AUTH_REQUEST";

    /**
     * 认证 Token
     */
    private String accessToken;

    public static String getTYPE() {
        return TYPE;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
