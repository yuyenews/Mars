package com.mars.common.ncfg.mvc;

/**
 * 保存核心servlet类
 */
public class CoreServletClass {

    /**
     * 核心servlet类
     */
    private static Class<?> cls;

    public static Class<?> getCls() {
        return cls;
    }

    public static void setCls(Class<?> cls) {
        CoreServletClass.cls = cls;
    }
}
