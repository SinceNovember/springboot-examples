package com.simple.jpa;

import com.simple.jpa.entity.UserDO;
import com.simple.jpa.repository.users.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSelectById() {
        UserDO user = userRepository.findById(1)
                .orElse(null); // 为空，则返回 null
        System.out.println(user);
    }

}