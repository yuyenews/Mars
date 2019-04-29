package com.mars.netty.util;

import com.mars.core.util.MesUtil;
import com.mars.server.server.request.HttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 响应工具类
 */
public class ResponseUtil {

    /**
     * 禁止访问响应
     * @param ctx
     */
    public static void sendForBidden(ChannelHandlerContext ctx, String ex){
        HttpResponse response = new HttpResponse(ctx);
        response.send(MesUtil.getMes(403,ex).toJSONString(), HttpResponseStatus.FORBIDDEN);
    }

    /**
     * 出错响应
     * @param ctx
     */
    public static void sendServerError(ChannelHandlerContext ctx, String ex){
        HttpResponse response = new HttpResponse(ctx);
        response.send(MesUtil.getMes(500,ex).toJSONString(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 请求超时响应
     * @param ctx
     */
    public static void sendTimeout(ChannelHandlerContext ctx,String ex){
        HttpResponse response = new HttpResponse(ctx);
        response.send(MesUtil.getMes(504,ex).toJSONString(), HttpResponseStatus.GATEWAY_TIMEOUT);
    }

}
