package com.mars.aio.server.factory;

import com.mars.aio.server.MarsServerHandler;
import com.mars.aio.server.impl.MarsServerDefaultHandler;

/**
 * 设置要用的联络器
 */
public class MarsServerHandlerFactory {

    /**
     * 默认使用MarsServerDefaultHandler
     */
    private static MarsServerHandler marsServerHandler = new MarsServerDefaultHandler();

    public static MarsServerHandler getMarsServerHandler() {
        return marsServerHandler;
    }

    public static void setMarsServerHandler(MarsServerHandler marsServerHandler) {
        MarsServerHandlerFactory.marsServerHandler = marsServerHandler;
    }
}
