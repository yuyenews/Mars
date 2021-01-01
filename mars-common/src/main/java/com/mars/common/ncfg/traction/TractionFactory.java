package com.mars.common.ncfg.traction;

/**
 * 保存管理事务类
 */
public class TractionFactory {

    /**
     * 管理事务类
     */
    private static Object traction;

    public static Object getTraction() {
        return traction;
    }

    public static void setTraction(Object traction) {
        TractionFactory.traction = traction;
    }
}
