package com.simple.custom.argreturnhandler.converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.simple.custom.argreturnhandler.entity.User;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 自己实现的HttpMessageConverter，请求报文转换为实体需要通过此方法实现
 */
public class MyHttpMessageConverter extends AbstractGenericHttpMessageConverter<User> {

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        // 判断支持处理哪些类型
        return User.class.isAssignableFrom(clazz);
    }
    @Override
    protected void writeInternal(User user, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        System.out.println("消息转换");
        // 测试
        user.setUsername("王五");
        // 这里参照 MappingJackson2HttpMessageConverter
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(httpOutputMessage.getBody(), JsonEncoding.UTF8);
        ObjectWriter objectWriter = objectMapper.writer();
        objectWriter.writeValue(generator, user);
        generator.flush();
    }



    @Override
    public User read(Type type, Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected User readInternal(Class<? extends User> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }
}
