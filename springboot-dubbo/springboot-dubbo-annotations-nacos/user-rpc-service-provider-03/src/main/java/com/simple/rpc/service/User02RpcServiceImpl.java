package com.simple.rpc.service;

import com.simple.rpc.api.UserRpcService;
import com.simple.rpc.dto.UserDTO;
import org.apache.dubbo.config.annotation.Service;


@Service(version = "${dubbo.provider.UserRpcService.version}", group = "user02")
public class User02RpcServiceImpl implements UserRpcService {
    @Override
    public UserDTO get(Integer id) {
        return new UserDTO().setId(id)
                .setName("[02]没有昵称：" + id)
                .setGender(id % 2 + 1); // 1 - 男；2 - 女
    }
}
