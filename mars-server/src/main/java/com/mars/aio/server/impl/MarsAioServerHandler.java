package com.mars.aio.server.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MesUtil;
import com.mars.aio.constant.HttpConstant;
import com.mars.aio.server.helper.MarsHttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 服务联络器
 */
public class MarsAioServerHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private Logger logger = LoggerFactory.getLogger(MarsAioServerHandler.class);

    /**
     * 处理请求
     * @param channel
     * @param serverSocketChannel
     */
    @Override
    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel serverSocketChannel) {
        serverSocketChannel.accept(serverSocketChannel, this);

        MarsHttpExchange marsHttpExchange = new MarsHttpExchange();
        marsHttpExchange.setSocketChannel(channel);

        try {
            /* 读取数据 */
            MarsHttpHelper.read(channel, marsHttpExchange);

            /* 过滤掉非法读取 */
            if (marsHttpExchange.getRequestURI() == null
                    || marsHttpExchange.getRequestMethod() == null
                    || marsHttpExchange.getHttpVersion() == null) {
                return;
            }

            /* 执行业务处理并响应数据 */
            MarsHttpHelper.write(marsHttpExchange);
        } catch (Exception e) {
            logger.error("处理请求异常", e);
            errorResponseText(e, marsHttpExchange);
        } finally {
            close(channel);
        }
    }

    /**
     * 连接失败
     * @param exc
     * @param serverSocketChannel
     */
    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel serverSocketChannel) {
        logger.error("建立连接异常", exc);
        serverSocketChannel.accept(serverSocketChannel, this);
    }

    /**
     * 异常的时候给前端一个响应
     * @param e
     */
    private void errorResponseText(Exception e, MarsHttpExchange marsHttpExchange){
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
    private void close(AsynchronousSocketChannel socketChannel) {
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (Exception e) {
        }
    }
}
