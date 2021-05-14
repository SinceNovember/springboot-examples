package com.simple.custom.rsa.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.custom.rsa.annotation.SecurityParameter;
import com.simple.custom.rsa.properties.ReadProBean;
import com.simple.custom.rsa.util.RSAUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;

/**
 * @author : Mr.Yan
 * @program : com
 * @create : 2019-03-27 18:41
 * @description : 对 @RequestMapping 返回值进行加密
 */
@ControllerAdvice
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice {

    /**
     * 加载配置文件信息
     */
    @Autowired
    private ReadProBean readProBean;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //这里设置成false 它就不会再走这个类了
        return methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try {
            System.out.println("开始对返回值进行加密操作!");
            // 定义是否解密
            boolean encode = false;
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //入参是否需要解密
            encode = serializedField.outEncode();
            System.out.println("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行加密!");
            return encryptedBody(body,encode);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行加密出现异常：" + e.getMessage());
            throw new RuntimeException("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行加密出现异常：" + e.getMessage());
        }
    }

    public Object encryptedBody(Object body,Boolean encode) throws IOException{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            if(!encode){
                System.out.println("没有加密表示不进行加密!");
                return body;
            }
            System.out.println("对字符串开始加密!");
            try {
                String outputData = RSAUtils.encryptedDataOnJava(result, readProBean.getPublicKey());
                JSONObject json = new JSONObject();
                json.put(readProBean.getResponseOutput(),outputData);
                System.out.println("操作结束!");
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException ("对返回数据加密出现异常!");
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException ("获取返回值出现异常!");
        }
    }
}