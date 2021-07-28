package com.simple.mybatis;

import com.simple.mybatis.dao.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        UserDO user = new UserDO();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("nicai");
        user.setCreateTime(new Date());
        userMapper.insert(user);
    }

    @Test
    public void testUpdateById() {
        UserDO updateUser = new UserDO();
        updateUser.setId(1);
        updateUser.setPassword("wobucai");

        userMapper.updateById(updateUser);
    }

    @Test
    public void testDeleteById() {
        userMapper.deleteById(2);
    }

    @Test
    public void testSelectById() {
        userMapper.selectById(1);
    }

    @Test
    public void testSelectByUsername() {
        userMapper.selectByUsername("yunai");
    }

    @Test
    public void testSelectByIds() {
        List<UserDO> users = userMapper.selectByIds(Arrays.asList(1, 3));
        System.out.println("users：" + users.size());
    }

}