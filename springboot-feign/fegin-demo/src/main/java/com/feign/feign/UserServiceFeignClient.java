package com.hystrix.feign;

import com.hystrix.feign.request.UserAddRequest;
import com.hystrix.feign.response.UserResponse;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;


public interface UserServiceFeignClient {

    @RequestLine("GET /user/get?id={id}")
    UserResponse get(@Param("id") Integer id);

    @RequestLine("GET /user/list?name={name}&gender={gender}")
    List<UserResponse> list(@Param("name") String name,
                            @Param("gender") Integer gender);

    @RequestLine("GET /user/list")
    List<UserResponse> list(@QueryMap Map<String, Object> queryMap);

    @RequestLine("POST /user/add")
    @Headers("Content-Type: application/json")
    Integer add(UserAddRequest request);



}
