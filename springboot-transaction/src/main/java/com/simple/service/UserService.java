package com.simple.service;

import com.simple.entity.User;
import com.simple.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    public void addUser() {
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
            transactionTemplate.executeWithoutResult(transactionStatus -> {
                userMapper.insertUser(new User(2, "test"));
            });


    }
}
