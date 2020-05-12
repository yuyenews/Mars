package com.mars.tomcat.util;

import com.mars.common.util.MesUtil;
import com.mars.server.server.request.HttpMarsResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应工具类
 */
public class ResponseUtil {

    /**
     * 出错响应
     * @param response
     */
    public static void sendServerError(HttpServletResponse response, String ex){
        HttpMarsResponse marsResponse = new HttpMarsResponse(response);
        marsResponse.send(MesUtil.getMes(500,ex).toJSONString());
    }
}
