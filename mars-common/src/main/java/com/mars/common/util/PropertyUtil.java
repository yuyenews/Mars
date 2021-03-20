package com.mars.common.util;

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
    public static <T> T copy(Object from, Class<T> to) throws Exception {
        if(from == null){
            return null;
        }
        return JSONUtil.toJavaObject(from, to);
    }
}
