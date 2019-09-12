package com.mars.server.server.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;
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

        if (header != null) {
            for (String key : header.keySet()) {
                response.headers().set(key, header.get(key));
            }
        }

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 设置跨域
     */
    private void crossDomain(FullHttpResponse response) {
        try{
            JSONObject jsonObject = getConfig();
            Object object = jsonObject.get("cross_domain");
            if (object != null) {
                JSONObject ob = JSONObject.parseObject(JSON.toJSONString(object));

                response.headers().set("Access-Control-Allow-Origin", ob.get("origin").toString());
                response.headers().set("Access-Control-Allow-Methods", ob.get("methods").toString());
                response.headers().set("Access-Control-Max-Age", ob.get("maxAge").toString());
                response.headers().set("Access-Control-Allow-Headers", ob.get("headers").toString());
                response.headers().set("Access-Control-Allow-Credentials", ob.get("credentials").toString());
            }
        } catch (Exception e){

            this.header.put("Access-Control-Allow-Origin", "*");
            this.header.put("Access-Control-Allow-Methods", "GET,POST");
            this.header.put("Access-Control-Max-Age", "9");
            this.header.put("Access-Control-Allow-Headers", "x-requested-with,Cache-Control,Pragma,Content-Type,Token");
            this.header.put("Access-Control-Allow-Credentials", "true");

            logger.warn("跨域配置缺少参数，已启动默认配置",e);
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
