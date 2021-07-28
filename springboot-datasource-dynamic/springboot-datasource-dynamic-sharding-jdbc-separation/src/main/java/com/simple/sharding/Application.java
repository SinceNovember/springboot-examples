package com.simple.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan(basePackages = "com.simple.sharding.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) // http://www.voidcn.com/article/p-zddcuyii-bpt.html
public class Application {
}