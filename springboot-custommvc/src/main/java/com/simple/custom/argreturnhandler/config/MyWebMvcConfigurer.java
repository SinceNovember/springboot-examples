package com.simple.custom.argreturnhandler.config;

import com.simple.custom.argreturnhandler.converter.MyHttpMessageConverter;
import com.simple.custom.argreturnhandler.handler.MyHandlerMethodReturnValueHandler;
import com.simple.custom.argreturnhandler.resolver.MyHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyWebMvcConfigurer  extends WebMvcConfigurationSupport {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        // 获取内建的resolvers
        List<HandlerMethodArgumentResolver> buildInList = requestMappingHandlerAdapter.getArgumentResolvers();
        // 重建resolvers
        List<HandlerMethodArgumentResolver> newResolvers = new ArrayList<>(buildInList.size() + 1);
        // 自定义的放到第一位,如果支持每个参数只会调用一个解析器
        newResolvers.add(new MyHandlerMethodArgumentResolver());
        // 再放内建的
        newResolvers.addAll(buildInList);
        // 重设resolvers
        requestMappingHandlerAdapter.setArgumentResolvers(newResolvers);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 添加自定义处理器，且放到首位
        converters.add(0, new MyHttpMessageConverter());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
        returnValueHandlers.add(new MyHandlerMethodReturnValueHandler());
    }
}
