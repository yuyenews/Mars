package com.mars.iserver.server.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.iserver.constant.HttpConstant;
import com.mars.iserver.server.model.MarsHttpExchangeModel;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 请求解析器
 */
public class MarsHttpExchange extends MarsHttpExchangeModel  {

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
    public void responseData() throws IOException {
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
    private void responseFile() throws IOException {
        /* 加载响应头 */
        setResponseHeader(MarsConstant.CONTENT_TYPE, "application/octet-stream");
        StringBuffer buffer = getCommonResponse(responseBody.length);

        /* 转成ByteBuffer */
        byte[] bytes = buffer.toString().getBytes(MarsConstant.ENCODING);

        /* 加载要响应的数据 */
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + responseBody.length);
        byteBuffer.put(bytes);
        byteBuffer.put(responseBody);

        byteBuffer.flip();

        /* 开始响应 */
        while (byteBuffer.hasRemaining()){
            socketChannel.write(byteBuffer);
        }
    }

    /**
     * 响应字符串
     *
     * @return
     */
    public void responseText(String text) throws IOException {
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
        while (byteBuffer.hasRemaining()){
            socketChannel.write(byteBuffer);
        }
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
