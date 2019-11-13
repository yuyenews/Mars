package com.mars.server.server.request;

import com.mars.server.server.request.model.CrossDomain;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应对象，对netty原生response的扩展
 * <p>
 * 暂时没有提供response的支持
 *
 * @author yuye
 */
public class HttpMarsResponse {

    /**
     * netty原生通道
     */
    private ChannelHandlerContext ctx;

    /**
     * 响应头
     */
    private Map<String, String> header;


    /**
     * 构造函数，框架自己用的，程序员用不到，用了也没意义
     *
     * @param ctx netty原生通道
     */
    public HttpMarsResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.header = new HashMap<>();
    }

    /**
     * 获取netty原生通道
     * @return netty原生通道
     */
    public ChannelHandlerContext getChannelHandlerContext() {
        return ctx;
    }

    /**
     * 设置响应头
     *
     * @param key   键
     * @param value 值
     */
    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    /**
     * 响应数据
     *
     * @param context 消息
     */
    public void send(String context) {
        send(context, HttpResponseStatus.OK);
    }


    /**
     * 响应数据
     *
     * @param context 消息
     * @param status  状态
     */
    public void send(String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));

        crossDomain(response);
        loadHeader(response);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 加载设置的header
     * @param response
     */
    private void loadHeader(FullHttpResponse response){
        if (header != null && !header.isEmpty()) {
            for (String key : header.keySet()) {
                response.headers().set(key, header.get(key));
            }
        }
    }

    /**
     * 设置跨域
     */
    private void crossDomain(FullHttpResponse response) {
        CrossDomain crossDomain = CrossDomain.getCrossDomain();
        response.headers().set("Access-Control-Allow-Origin", crossDomain.getOrigin());
        response.headers().set("Access-Control-Allow-Methods", crossDomain.getMethods());
        response.headers().set("Access-Control-Max-Age", crossDomain.getMaxAge());
        response.headers().set("Access-Control-Allow-Headers", crossDomain.getHeaders());
        response.headers().set("Access-Control-Allow-Credentials", crossDomain.getCredentials());
    }
}
