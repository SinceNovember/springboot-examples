package com.simple.custom.rsa.controller;

import com.simple.custom.rsa.annotation.SecurityParameter;
import com.simple.custom.rsa.util.ResultModel;
import com.simple.custom.rsa.util.ResultTools;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Mr.Yan
 * @program : com
 * @create : 2019-03-27 17:58
 * @description :
 */
@RestController
public class LoginController {

    /**
     * @description: 自动对参数加密解密
     */
    @SecurityParameter(inDecode = true,outEncode = true)
    @RequestMapping("/login")
    public ResultModel login(HttpServletResponse response, HttpServletRequest request,
                             @RequestBody(required = false)  Map<String,Object> map){
        System.out.println("进入login后台");
        System.out.println("输出account:————————————————" + map.get("account"));
        System.out.println("输出password:————————————————" + map.get("password"));

        Map<String,Object> resultMap =  new HashMap<>();
        resultMap.put("userid","200");
        return ResultTools.result(0,"",resultMap);
    }
}