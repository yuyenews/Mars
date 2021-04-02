package com.mars.aio.server.impl;

import com.mars.aio.server.helper.MarsHttpHelper;
import com.mars.common.base.config.model.RequestConfig;
import com.mars.common.constant.MarsConstant;
import com.mars.aio.constant.HttpConstant;
import com.mars.aio.server.model.MarsHttpExchangeModel;
import com.mars.common.util.MarsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求解析器
 */
public class MarsHttpExchange extends MarsHttpExchangeModel  {

    private Logger logger = LoggerFactory.getLogger(MarsHttpExchange.class);

    /**
     * 请求配置
     */
    private RequestConfig requestConfig = MarsConfiguration.getConfig().requestConfig();

    /**
     * 实例化
     */
    public MarsHttpExchange(){
        super();
    }

    /**
     * 执行响应操作
     * @throws IOException
     */
    public void responseData() throws Exception {
        if(responseBody != null){
            /* 只要响应流不为空，就按文件下载处理 */
            responseFile();
        } else {
            responseText(null);
        }
    }

    /**
     * 响应文件流
     */
    private void responseFile() throws Exception {
        /* 加载响应头 */
        setResponseHeader(MarsConstant.CONTENT_TYPE, "application/octet-stream");
        StringBuffer buffer = getCommonResponse(responseBody.size());

        /* 转成ByteBuffer */
        byte[] bytes = buffer.toString().getBytes(MarsConstant.ENCODING);

        /* 加载要响应的数据 */
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + responseBody.size());
        byteBuffer.put(bytes);
        byteBuffer.put(responseBody.toByteArray());

        byteBuffer.flip();
        doWrite(byteBuffer);
    }

    /**
     * 响应字符串
     *
     * @return
     */
    public void responseText(String text) throws Exception {
        if(text == null){
            text = sendText;
        }
        if(text.equals(MarsConstant.VOID)){
            return;
        }
        /* 加载响应头 */
        StringBuffer buffer = getCommonResponse(text.getBytes().length);

        /* 加载要响应的数据 */
        buffer.append(text);

        /* 转成ByteBuffer */
        byte[] bytes = buffer.toString().getBytes(MarsConstant.ENCODING);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);

        byteBuffer.flip();

        /* 开始响应 */
        doWrite(byteBuffer);
    }

    /**
     * 往客户端写数据
     * @param byteBuffer
     */
    private void doWrite(ByteBuffer byteBuffer) {
        socketChannel.write(byteBuffer, requestConfig.getWriteTimeout(), TimeUnit.MILLISECONDS, byteBuffer,
                new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                try {
                    if(attachment.hasRemaining()){
                        socketChannel.write(attachment,requestConfig.getWriteTimeout(),
                                TimeUnit.MILLISECONDS, attachment,this);
                    } else {
                        MarsHttpHelper.close(socketChannel, false);
                        MarsHttpHelper.closeOutputStream(responseBody);
                    }
                } catch (Exception e){
                    logger.error("给客户端写入响应数据异常", e);
                    MarsHttpHelper.close(socketChannel,false);
                    MarsHttpHelper.closeOutputStream(responseBody);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                logger.error("给客户端写入响应数据异常", exc);
                MarsHttpHelper.close(socketChannel,false);
                MarsHttpHelper.closeOutputStream(responseBody);
            }
        });
    }

    /**
     * 获取公共的返回信息
     * @return
     */
    private StringBuffer getCommonResponse(int length){
        StringBuffer buffer = new StringBuffer();

        /* 加载初始化头 */
        buffer.append(HttpConstant.BASIC_RESPONSE.replace("{statusCode}", String.valueOf(statusCode)));
        buffer.append(HttpConstant.CARRIAGE_RETURN);
        buffer.append(MarsConstant.CONTENT_LENGTH + ": " + length);
        buffer.append(HttpConstant.CARRIAGE_RETURN);

        /* 加载自定义头 */
        for(Map.Entry<String, String> entry : responseHeaders.entrySet()){
            String value = entry.getValue();
            if(value == null){
                continue;
            }
            buffer.append(entry.getKey() + ":" + value);
            buffer.append(HttpConstant.CARRIAGE_RETURN);
        }
        buffer.append(HttpConstant.CARRIAGE_RETURN);
        return buffer;
    }

    /**
     * 获取本次的请求类型
     * @return
     */
    public String getContentType(){
        String contentType = requestHeaders.get(MarsConstant.CONTENT_TYPE);
        if(contentType == null){
            contentType = requestHeaders.get(MarsConstant.CONTENT_TYPE_LOW);
        }
        return contentType;
    }

    /**
     * 获取请求长度
     * @return
     */
    public long getRequestContentLength(){
        String contentLength = requestHeaders.get(MarsConstant.CONTENT_LENGTH);
        if(contentLength == null){
            contentLength = requestHeaders.get(MarsConstant.CONTENT_LENGTH_LOW);
        }

        if(contentLength == null){
            return -1;
        }
        return Long.parseLong(contentLength);
    }
}
