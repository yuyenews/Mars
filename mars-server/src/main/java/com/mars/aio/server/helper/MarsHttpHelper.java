package com.mars.aio.server.helper;

import com.mars.aio.server.MarsServerHandler;
import com.mars.aio.server.factory.MarsServerHandlerFactory;
import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MesUtil;
import com.mars.aio.constant.HttpConstant;
import com.mars.aio.server.impl.MarsHttpExchange;

import java.io.OutputStream;
import java.nio.channels.*;

/**
 * Http帮助类
 */
public class MarsHttpHelper {

    /**
     * 异常的时候给前端一个响应
     * @param e
     */
    public static void errorResponseText(Throwable e, MarsHttpExchange marsHttpExchange){
        try {
            marsHttpExchange.setResponseHeader(MarsConstant.CONTENT_TYPE, HttpConstant.RESPONSE_CONTENT_TYPE);
            marsHttpExchange.responseText(MesUtil.getMes(500,"处理请求异常:" + e.getMessage()));
        } catch (Exception ex){
        }
    }

    /**
     * 释放资源
     *
     * @param socketChannel
     */
    public static void close(AsynchronousSocketChannel socketChannel, boolean shutInput) {
        try {
            if (socketChannel != null) {
                if(shutInput){
                    socketChannel.shutdownInput();
                }
                socketChannel.shutdownOutput();
                socketChannel.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 响应
     *
     */
    public static void write(MarsHttpExchange marsHttpExchange) throws Exception {
        /* 执行handler */
        MarsServerHandler marsServerHandler = MarsServerHandlerFactory.getMarsServerHandler();
        marsServerHandler.request(marsHttpExchange);

        /* 响应数据 */
        marsHttpExchange.responseData();
    }

    /**
     * 关闭输出流
     * @param outputStream
     */
    public static void closeOutputStream(OutputStream outputStream) {
        if(outputStream == null){
            return;
        }
        try {
            outputStream.close();
        } catch (Exception e){
        }
    }
}
