package com.mars.common.ncfg.mvc;

/**
 * 保存核心servlet类
 */
public class CoreServletClass {

    /**
     * 核心servlet类
     */
    private static Object object;

    public static Object getObject() {
        return object;
    }

    public static void setObject(Object object) {
        CoreServletClass.object = object;
    }
}
