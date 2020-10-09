package com.mars.iserver.server.factory;

import com.mars.iserver.server.MarsServer;
import com.mars.iserver.server.impl.MarsDefaultServer;

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
