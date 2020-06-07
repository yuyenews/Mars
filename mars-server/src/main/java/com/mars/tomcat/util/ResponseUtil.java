package com.mars.tomcat.util;

import com.mars.common.util.MesUtil;
import com.mars.server.server.request.HttpMarsResponse;
import com.sun.net.httpserver.HttpExchange;

/**
 * 响应工具类
 */
public class ResponseUtil {

    /**
     * 出错响应
     * @param httpExchange
     */
    public static void sendServerError(HttpExchange httpExchange, String ex){
        HttpMarsResponse marsResponse = new HttpMarsResponse(httpExchange);
        marsResponse.send(MesUtil.getMes(500,ex).toJSONString());
    }
}
