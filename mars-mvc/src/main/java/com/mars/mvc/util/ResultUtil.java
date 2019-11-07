package com.mars.mvc.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class ResultUtil {

    /**
     * 成功的返回数据结构
     * @param obj bean的返回值
     * @return json
     */
    public static JSONObject getSuccessResult(Object obj){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error_code","success");
        jsonObject.put("result",obj);
        return jsonObject;
    }

    /**
     * 失败的返回数据结构
     * @param obj 错误提示
     * @return json
     */
    public static JSONObject getFailResult(Object obj){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error_code","fail");
        jsonObject.put("error_info",obj);
        return jsonObject;
    }

    /**
     * 异常的返回数据结构
     * @param e 错误提示
     * @return json
     */
    public static JSONObject getErrorResult(Throwable e){
        if(e instanceof InvocationTargetException){
            InvocationTargetException targetException = (InvocationTargetException)e;
            return getFailResult(targetException.getTargetException().getMessage());
        }
        return getFailResult(e.getMessage());
    }
}
