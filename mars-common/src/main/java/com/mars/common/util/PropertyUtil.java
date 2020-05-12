package com.mars.common.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 属性工具类
 */
public class PropertyUtil {

    /**
     * 拷贝属性
     * @param from 原始对象
     * @param to 要拷贝到的对象类型
     * @param <T> 要拷贝到的对象类型
     * @return 拷贝到的对象
     */
    public static <T> T copy(Object from, Class<T> to){
        if(from == null){
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(from));
        return jsonObject.toJavaObject(to);
    }
}
