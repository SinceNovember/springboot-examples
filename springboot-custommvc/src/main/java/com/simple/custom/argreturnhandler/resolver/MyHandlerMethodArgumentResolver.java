package com.simple.custom.argreturnhandler.resolver;

import com.simple.custom.argreturnhandler.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义参数解析器,参数解析器只会调用符合条件的一个，所有的Handler参数都会先调用此方法进行处理
 */
public class MyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 参数类型判断
        return User.class.isAssignableFrom(parameter.getParameterType());
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // TODO do resolve
        // 这里参数解析暂时写死
        return new User("李四");
    }
}
