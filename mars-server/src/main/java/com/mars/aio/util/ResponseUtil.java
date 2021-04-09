package com.mars.aio.util;

import com.mars.common.util.MesUtil;
import com.mars.aio.server.request.HttpMarsResponse;

/**
 * 响应工具类
 */
public class ResponseUtil {

    /**
     * 出错响应
     * @param marsResponse
     */
    public static void sendServerError(HttpMarsResponse marsResponse, String ex){
        marsResponse.send(MesUtil.getMes(500,ex));
    }
}
