package com.yuyenews.easy.server.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.logger.GogeLogger;
import com.yuyenews.core.util.ConfigUtil;
import com.yuyenews.core.util.FileUtil;
import com.yuyenews.core.util.MesUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
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

    private GogeLogger logger = GogeLogger.getLogger(HttpResponse.class);

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
     * 文件下载
     *
     * @param file 要下载的文件
     */
    public void sendFile(File file) {
        try{
            setHeader("Content-Length", String.valueOf(file.length()));
            sendFile(FileUtil.getFileToByte(file),file.getName());
        } catch (Exception e){
            logger.error("将流文件流响应给客户端出错",e);
        }
    }

    /**
     * 文件下载
     *
     * @param file 要下载的文件
     */
    public void sendFile(InputStream file,String fileName) {
        sendFile(FileUtil.getInputStreamToByte(file),fileName);
    }

    /**
     * 文件下载
     *
     * @param file 要下载的文件
     */
    public void sendFile(BufferedImage file, String fileName){
        sendFile(FileUtil.getBufferedImageToByte(file),fileName);
    }

    /**
     * 文件下载
     *
     * @param file 要下载的文件
     */
    public void sendFile(byte[] file,String fileName) {
        try{

            if(file == null){
                if(this.header.get("Content-Length") != null){
                    this.header.remove("Content-Length");
                }
                send(MesUtil.getMes(404,"要下载的文件不存在").toJSONString());
                throw new Exception("要下载的文件不存在");
            }

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(file));

            crossDomain(response);

            if (header != null) {
                for (String key : header.keySet()) {
                    response.headers().set(key, header.get(key));
                }
            }
            response.headers().set("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),"UTF-8"));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e){
            logger.error("将流文件流响应给客户端出错",e);
        }
    }

    /**
     * 设置跨域
     */
    private void crossDomain(FullHttpResponse response) {
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
