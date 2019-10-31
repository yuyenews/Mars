package com.mars.mvc.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 将API的参数转成引用的方法的参数
 */
public class ParamUtil {

    /**
     * 将API的参数转成引用的方法的参数
     * @param paramTypes 参数类型列表
     * @param args 参数值列表
     * @return
     * @throws Exception
     */
    public static Object[] getServiceParams(Class<?>[] paramTypes, Object[] args) throws Exception {
        try {
            if(paramTypes == null || paramTypes.length < 0){
                return null;
            }
            if(args == null || args.length < 0){
                return new Object[paramTypes.length];
            }

            Object[] params = new Object[paramTypes.length];

            /* 把api的参数值拿出来，转成json */
            JSONObject jsonObject = new JSONObject();
            for(Object arg : args){
                String jsonString = JSON.toJSONString(arg);
                JSONObject argJson = JSONObject.parseObject(jsonString);
                jsonObject.putAll(argJson);
            }

            /* 把api的参数值 分别赋值到引用的方法的参数里 */
            for(int i=0;i<paramTypes.length;i++){
                Class paramType = paramTypes[i];
                params[i] = jsonObject.toJavaObject(paramType);
            }
            return params;
        } catch (Exception e){
            throw new Exception("将API的参数适配到引用的方法上出异常了，请核对是否满足以下情况: [API所引用的方法的参数只可以使用自定对象或者Map]",e);
        }
    }
}
