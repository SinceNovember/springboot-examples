package com.simple.rpc.service;

import com.simple.rpc.api.UserRpcService;
import com.simple.rpc.core.ServiceException;
import com.simple.rpc.core.ServiceExceptionEnum;
import com.simple.rpc.dto.UserAddDTO;
import com.simple.rpc.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

@Service
public class UserRpcServiceImpl implements UserRpcService {
    @Override
    public UserDTO get(Integer id) {
        return new UserDTO().setId(id)
                .setName("没有昵称：" + id)
                .setGender(id % 2 + 1); // 1 - 男；2 - 女
    }

    @Override
    public Integer add(UserAddDTO addDTO) {
        // 这里，模拟用户已经存在的情况
        if ("yudaoyuanma".equals(addDTO.getName())) {
            throw new ServiceException(ServiceExceptionEnum.USER_EXISTS);
        }
        return (int) (System.currentTimeMillis() / 1000); // 嘿嘿，随便返回一个 id
    }
}
