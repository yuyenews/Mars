package com.mars.core.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 错误提示信息 工具类
 */
public class MesUtil {

    /**
     * 获取错误提示信息
     * @param errorCode code
     * @param errorMsg 信息
     * @return 异常
     */
    public static JSONObject getMes(Integer errorCode,String errorMsg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error_code", errorCode);
        jsonObject.put("error_info", errorMsg);
        return jsonObject;
    }
}
