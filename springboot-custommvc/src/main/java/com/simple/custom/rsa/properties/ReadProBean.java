package com.simple.custom.rsa.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rsa")
public class ReadProBean {
    /**
     * RSA 加密定义公钥
     */
    private String publicKey;

    /**
     * RSA 加密定义私钥
     */
    private String privateKey;

    /**
     * 前台发送的加密的json串命名
     */
    private String requestInput;

    /**
     * 后台发送的加密的json串命名
     */
    private String responseOutput;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getRequestInput() {
        return requestInput;
    }

    public void setRequestInput(String requestInput) {
        this.requestInput = requestInput;
    }

    public String getResponseOutput() {
        return responseOutput;
    }

    public void setResponseOutput(String responseOutput) {
        this.responseOutput = responseOutput;
    }
}
