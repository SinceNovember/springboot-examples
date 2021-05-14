package com.simple.custom.rsa.util;

import java.util.Map;

public class ResultTools {
    /****
     * 错误码记录：
     * 0--------成功
     * 404------异常抛出错误
     *
     */

    public static ResultModel result(int code, String msg, Map<String, Object> map) {
        ResultModel model = new ResultModel();
        model.setCode(code);
        switch (code) {
            case 0:
                model.setMsg("成功");
                if (map != null) {
                    model.setData(map);
                }
                break;
            default:
                model.setMsg(msg);
                break;
        }
        return model;
    }
}
