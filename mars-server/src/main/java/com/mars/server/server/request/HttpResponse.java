package com.mars.server.server.request;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;
import com.mars.server.server.request.model.CrossDomain;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应对象，对netty原生response的扩展
 * <p>
 * 暂时没有提供response的支持
 *
 * @author yuye
 */
public class HttpResponse {

    private Logger logger = LoggerFactory.getLogger(HttpResponse.class);

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
    public HttpResponse(ChannelHandlerContext ctx) {
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
        try {
            JSONObject object = getConfig().getJSONObject("cross_domain");
            CrossDomain.origin = object.get("origin").toString();
            CrossDomain.methods = object.get("methods").toString();
            CrossDomain.maxAge = object.get("maxAge").toString();
            CrossDomain.headers = object.get("headers").toString();
            CrossDomain.credentials = object.get("credentials").toString();
        } catch (Exception e) {
            logger.warn("跨域配置缺少参数，已启动默认配置", e);
        } finally {
            response.headers().set("Access-Control-Allow-Origin", CrossDomain.origin);
            response.headers().set("Access-Control-Allow-Methods", CrossDomain.methods);
            response.headers().set("Access-Control-Max-Age", CrossDomain.maxAge);
            response.headers().set("Access-Control-Allow-Headers", CrossDomain.headers);
            response.headers().set("Access-Control-Allow-Credentials", CrossDomain.credentials);
        }
    }

    /**
     * 获取配置文件
     *
     * @return 配置文件对象
     */
    private JSONObject getConfig() {
        JSONObject jsonObject = ConfigUtil.getConfig();
        if (jsonObject != null) {
            return jsonObject;
        }

        return new JSONObject();
    }
}
