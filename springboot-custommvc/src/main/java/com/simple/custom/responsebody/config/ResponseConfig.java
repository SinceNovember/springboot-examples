package com.simple.custom.responsebody.config;

import com.simple.custom.responsebody.handler.ResultWarpReturnValueHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ResponseConfig implements WebMvcConfigurer, InitializingBean {

    @Autowired(required = false)
    private RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        // 新建一个List来保存替换后的Handler的List
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(returnValueHandlers);
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                // 创建定制的Json格式处理Handler
                ResultWarpReturnValueHandler decorator = new ResultWarpReturnValueHandler(handler);
                // 使用定制的Json格式处理Handler替换原有的RequestResponseBodyMethodProcessor
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
        // 重新设置SpringMVC的ReturnValueHandlers
        adapter.setReturnValueHandlers(handlers);
    }
}
