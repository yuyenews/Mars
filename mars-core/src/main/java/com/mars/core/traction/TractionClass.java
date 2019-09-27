package com.mars.core.traction;

/**
 * 保存管理事务类
 */
public class TractionClass {

    /**
     * 管理事务类
     */
    private static Class<?> cls;

    public static Class<?> getCls() {
        return cls;
    }

    public static void setCls(Class<?> cls) {
        TractionClass.cls = cls;
    }
}
