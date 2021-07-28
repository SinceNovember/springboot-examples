package com.nacos.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nacos自动刷新需要使用自带的注解
 */
@Component
@NacosConfigurationProperties(prefix = "", dataId = "${nacos.config.data-id}", type = ConfigType.YAML, autoRefreshed = true)
public class TestProperties {

    /**
     * 测试属性
     */
    private String test;

    public String getTest() {
        return test;
    }

    public TestProperties setTest(String test) {
        this.test = test;
        return this;
    }

}