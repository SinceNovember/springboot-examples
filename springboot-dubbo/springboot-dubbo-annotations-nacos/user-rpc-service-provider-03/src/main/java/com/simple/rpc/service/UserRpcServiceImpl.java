package com.simple.rpc.service;

import com.simple.rpc.api.UserRpcService;
import com.simple.rpc.dto.UserDTO;
import org.apache.dubbo.config.annotation.Service;


/**
 * 相同接口，不同实现 通过group来区分
 */
@Service(version = "${dubbo.provider.UserRpcService.version}", group = "user01")
public class UserRpcServiceImpl implements UserRpcService {
    @Override
    public UserDTO get(Integer id) {
        return new UserDTO().setId(id)
                .setName("没有昵称：" + id)
                .setGender(id % 2 + 1); // 1 - 男；2 - 女
    }
}
