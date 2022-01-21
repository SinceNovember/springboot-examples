package com.simple.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZooConfig {

    @Value("${zookeeper.address}")
    private String zkAddr;

    @Bean
    public CuratorFramework curatorFramework(){
//        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkAddr, 5000, 3000, new ExponentialBackoffRetry(1000, 5));
        //builder方式
        CuratorFramework zkClient = CuratorFrameworkFactory.builder().connectString(zkAddr)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        zkClient.start();
        return zkClient;
    }
}
