package com.simple.elasticsearch.vo;

public class ResponseResult {

    private int code;

    private String msg;

    private boolean status;

    private Object data;

    public ResponseResult() {
    }

    public static ResponseResult success(){
        return new ResponseResult(200, "成功", true, null);
    }

    public ResponseResult(int code, String msg, boolean status, Object data) {
        this.code = code;
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public enum ResponseCode
    {
        DUPLICATE_KEY_ERROR_CODE(501, "重复的key错误"),
        ERROR(502, "错误"),
        SUCCESS(200,"成功"),
        RESOURCE_NOT_EXIST(404,"资源不存在"),
        NETWORK_ERROR(503,"网络错误"),
        PARAM_ERROR_CODE(504,"参数错误");

        int code;

        String msg;

        ResponseCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
