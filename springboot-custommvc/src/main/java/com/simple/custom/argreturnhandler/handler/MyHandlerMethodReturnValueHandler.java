package com.simple.custom.argreturnhandler.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.invoke.MethodHandle;

/**
 * 自定义HandlerMethodReturnValueHandler，所有Handler方法返回值都先调用此方法进行处理。
 */
public class MyHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        System.out.println("MyHandlerMethodReturnValueHandler");
        return methodParameter.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        System.out.println("HandlerMethodReturnValueHandler调用");

    }
}
