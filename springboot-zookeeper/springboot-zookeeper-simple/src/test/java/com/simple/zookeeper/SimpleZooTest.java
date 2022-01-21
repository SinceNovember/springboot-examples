package com.simple.zookeeper;

import com.simple.zookeeper.service.ZKService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleZooTest {

    @Resource
    private ZKService zkService;

    @Test
    public void test() {
        zkService.lock("/nodetest");
//       zkService.createAndSetNodeInTransaction("/nodetesttrans", "asd");
    }
}
