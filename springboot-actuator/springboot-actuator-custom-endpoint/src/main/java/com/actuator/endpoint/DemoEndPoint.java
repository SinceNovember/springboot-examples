package com.actuator.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * http://127.0.0.1:8080/actuator/demo
 */
@Component
@Endpoint(id = "demo")
public class DemoEndPoint {

    @ReadOperation
    public Map<String, String> hello() {
        Map<String, String> result = new HashMap<>();
        result.put("作者", "yudaoyuanma");
        result.put("秃头", "true");
        return result;
    }
}