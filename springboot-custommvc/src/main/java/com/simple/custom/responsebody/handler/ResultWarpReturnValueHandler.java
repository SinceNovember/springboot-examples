package com.simple.custom.responsebody.handler;

import com.simple.custom.responsebody.entity.Result;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 统一处理ResponseBody数据格式
 **/
public class ResultWarpReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    /** 委托 */
    public ResultWarpReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 委托SpringMVC默认的RequestResponseBodyMethodProcessor进行序列化,不是Result类型的包装为Result类型
        delegate.handleReturnValue(returnValue instanceof Result ? returnValue : Result.success(returnValue), returnType, mavContainer, webRequest);
    }
}
