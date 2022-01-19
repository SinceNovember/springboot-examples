package com.simple.redissson.config;


import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        //单机
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        //sentinel哨兵主从配置 addSentinelAddress里配置sentinel地址
//        config.useSentinelServers().setMasterName("master").addSentinelAddress("redis://127.0.0.1:16380", "redis://127.0.0.1:26380", "redis://127.0.0.1:36380")
        //集群
//        config.useClusterServers()
//                .addNodeAddress("redis://127.0.0.1:6380")
//                .addNodeAddress("redis://127.0.0.1:6381")
//                .addNodeAddress("redis://127.0.0.1:6382")
//                .addNodeAddress("redis://127.0.0.1:6390")
//                .addNodeAddress("redis://127.0.0.1:6391")
//                .addNodeAddress("redis://127.0.0.1:6392");
        //主从配置
//        config.useMasterSlaveServers().setMasterAddress("redis://127.0.0.1:6380").setPassword("").setSlaveAddresses(new HashSet<String>(){{
//            add("redis://127.0.0.1:6380");
//        }});

        return Redisson.create(config);
    }

}

