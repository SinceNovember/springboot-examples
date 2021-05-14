package com.simple.custom.rsa.advice;

import com.simple.custom.rsa.annotation.SecurityParameter;
import com.simple.custom.rsa.properties.ReadProBean;
import com.simple.custom.rsa.util.RSAUtils;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@ControllerAdvice
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {

    /**
     * 加载配置文件信息
     */
    @Autowired
    private ReadProBean readProBean;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //这里设置成false 它就不会再走这个类了
        return methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class);    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            System.out.println("开始对接受值进行解密操作");
            // 定义是否解密
            boolean encode = false;
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //入参是否需要解密
            encode = serializedField.inDecode();
            System.out.println("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行解密!");
            return new MyHttpInputMessage(httpInputMessage,encode);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行解密出现异常：" + e.getMessage());
            throw new RuntimeException("对方法method :【" + methodParameter.getMethod().getName() + "】数据进行解密出现异常：" + e.getMessage());
        }
    }

    //这里实现了HttpInputMessage 封装一个自己的HttpInputMessage
    class MyHttpInputMessage implements HttpInputMessage {
        HttpHeaders headers;
        InputStream body;

        public MyHttpInputMessage(HttpInputMessage httpInputMessage,boolean encode) throws IOException {
            this.headers = httpInputMessage.getHeaders();
            // 解密操作
            this.body = decryptBody(httpInputMessage.getBody(),encode);
        }

        @Override
        public InputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

    /**
     * 对流进行解密操作
     *
     * @param in
     * @return
     */
    public InputStream decryptBody(InputStream in,Boolean encode) throws IOException {
        try {
            // 获取 json 字符串
            String bodyStr = IOUtils.toString(in, "UTF-8");
            if (StringUtils.isEmpty(bodyStr)) {
                throw new RuntimeException("无任何参数异常!");
            }
            // 获取 inputData 加密串
            String inputData = JSONObject.fromObject(bodyStr).get(readProBean.getRequestInput()).toString();

            // 验证是否为空
            if (StringUtils.isEmpty(inputData)) {
                throw new RuntimeException("参数【"+readProBean.getRequestInput()+"】缺失异常!");
            } else {

                // 是否解密
                if(!encode){
                    System.out.println("没有解密标识不进行解密!");
                    return IOUtils.toInputStream(inputData, "UTF-8");
                }

                // 开始解密
                System.out.println("对加密串开始解密操作!");
                String decryptBody = null;
                try {
                    decryptBody = RSAUtils.decryptDataOnJava(inputData, readProBean.getPrivateKey());
                    System.out.println("操作结束!");
                    return IOUtils.toInputStream(decryptBody, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("RSA解密异常!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取参数【"+readProBean.getRequestInput()+"】异常!!");
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
