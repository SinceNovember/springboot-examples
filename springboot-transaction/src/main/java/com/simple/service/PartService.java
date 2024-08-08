package com.simple.service;

import com.simple.entity.Part;
import com.simple.mapper.PartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Service
public class PartService {

    @Resource
    private PartMapper partMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private UserService userService;

    public void addPart() {
//        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        transactionTemplate.executeWithoutResult(transactionStatus ->{
                    partMapper.insertPart(new Part(2, "test"));
                    userService.addUser();
            throw new NullPointerException();

        } );
    }
}
