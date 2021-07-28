package com.simple.rpc.api;

import com.simple.rpc.dto.UserDTO;

import javax.validation.constraints.NotNull;

/**
 * 用户服务 RPC Service 接口
 */
public interface UserRpcService {

    /**
     * 根据指定用户编号，获得用户信息
     *
     * @param id 用户编号
     * @return 用户信息
     */
    UserDTO get(@NotNull(message = "用户编号不能为空") Integer id);

}