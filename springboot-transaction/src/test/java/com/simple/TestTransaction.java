package com.simple;


import com.simple.service.PartService;
import com.simple.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTransaction {

    @Resource
    private PartService partService;

    @Test
    public void test() {
        partService.addPart();
//        userService.addUser();
    }
}
