package com.mars.mj.util;

/**
 * 数据检查
 */
public class DataCheckUtil {

    /**
     * 非空判断
     * @param param
     * @param message
     * @throws Exception
     */
    public static void isNull(Object param,String message) throws Exception {
        if (param == null) {
            throw new Exception(message);
        }
    }
}
