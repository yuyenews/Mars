package com.mars.aio.par.factory;

import com.mars.aio.par.InitRequest;
import com.mars.aio.par.impl.InitRequestDefault;

/**
 * 初始化request数据工厂
 */
public class InitRequestFactory {

    private static InitRequest initRequest = new InitRequestDefault();

    public static InitRequest getInitRequest() {
        return initRequest;
    }

    public static void setInitRequest(InitRequest initRequest) {
        InitRequestFactory.initRequest = initRequest;
    }
}
