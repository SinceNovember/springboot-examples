package com.simple.custom.argreturnhandler.advice;

import com.simple.custom.argreturnhandler.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class MyResponseBodyAdvice1 implements ResponseBodyAdvice<User> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return User.class.isAssignableFrom(converterType);
    }

    @Override
    public User beforeBodyWrite(User body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println("操作responseBodyAdvice1");
        body.setUsername2("username2");
        return body;
    }
}
