package com.mars.aio.constant;

import com.mars.server.tcp.http.constant.ReqMethod;

public class ServerConstant {

    /**
     * 所有的请求方式
     */
    public static final ReqMethod[] REQ_METHODS = new ReqMethod[]{ReqMethod.GET, ReqMethod.POST, ReqMethod.DELETE, ReqMethod.PUT};
}
