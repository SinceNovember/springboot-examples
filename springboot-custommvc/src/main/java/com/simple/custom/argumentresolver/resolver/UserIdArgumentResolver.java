package com.simple.custom.argumentresolver.resolver;

import com.simple.custom.argumentresolver.annoation.UserId;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        UserId userIdAnnotation = methodParameter.getParameterAnnotation(UserId.class);
        Object id = "123";
        if (userIdAnnotation == null) {
            return null;
        } else if (id == null && userIdAnnotation.required()) {
            throw new IllegalArgumentException("No login");
        } else {
            return id;
        }
    }
}
