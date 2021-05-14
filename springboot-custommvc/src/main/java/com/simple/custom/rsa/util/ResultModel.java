package com.simple.custom.rsa.util;

import java.util.Map;

public class ResultModel {

    private int code ;//返回码

    private Map<String, Object> data;// 返回数据

    private String msg; //返回消息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
