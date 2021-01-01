package com.mars.common.ncfg.mvc;

/**
 * 保存核心servlet类
 */
public class DispatcherFactory {

    /**
     * 核心servlet类
     */
    private static Object dispatcher;

    public static Object getDispatcher() {
        return dispatcher;
    }

    public static void setDispatcher(Object dispatcher) {
        DispatcherFactory.dispatcher = dispatcher;
    }
}
