package com.simple.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PipelineTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01() {
        List<Object> results = stringRedisTemplate.executePipelined(
                (RedisCallback<Object>) connection ->{
                    // set 写入
                    for (int i = 0; i < 3; i++) {
                        connection.set(String.format("yunai:%d", i).getBytes(), "shuai".getBytes());
                    }

                    // get
                    for (int i = 0; i < 3; i++) {
                        connection.get(String.format("yunai:%d", i).getBytes());
                    }

                    // 返回 null 即可
                    return null;
                }
        );
        // 打印结果
        System.out.println(results);
    }
}
