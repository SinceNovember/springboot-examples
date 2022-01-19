package com.simple.cluster.config;

import com.simple.cluster.component.RedisClusterNodesCfg;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Profile("JedisCluster")
public class JedisClusterConfig {

    @Resource
    private RedisClusterNodesCfg redisClusterNodesCfg;

    /**
     * @Description Jedis连接工厂配置
     * @return
     */
    @Bean
    public JedisConnectionFactory redisPoolFactory() {
        RedisClusterConfiguration rcc = new RedisClusterConfiguration();
        List<String> nodeList = redisClusterNodesCfg.getNodes();
        nodeList.forEach(node -> {
            rcc.addClusterNode(new RedisNode(node.split(":")[0], Integer.valueOf(node.split(":")[1])));
        });

        return new JedisConnectionFactory(rcc);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
