package com.mars.aio.server.factory;

import com.mars.aio.server.MarsServer;
import com.mars.aio.server.impl.MarsDefaultServer;

/**
 * 获取要使用的服务
 */
public class MarsServerFactory {

    private static MarsServer marsServer = new MarsDefaultServer();

    public static MarsServer getMarsServer(){
        return marsServer;
    }

    public static void setMarsServer(MarsServer marsServer){
        MarsServerFactory.marsServer = marsServer;
    }
}
