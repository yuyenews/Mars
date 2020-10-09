package com.mars.iserver.par.factory;

import com.mars.iserver.par.InitRequest;
import com.mars.iserver.par.impl.InitRequestDefault;

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
