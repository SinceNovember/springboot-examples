package com.sofarpc.service;

import com.sofarpc.api.UserRpcService;
import com.sofarpc.dto.UserAddDTO;
import com.sofarpc.dto.UserDTO;
import org.springframework.stereotype.Service;

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
        return (int) (System.currentTimeMillis() / 1000); // 嘿嘿，随便返回一个 id
    }

}