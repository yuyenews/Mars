package com.mars.iserver.server.handler;

import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MesUtil;
import com.mars.iserver.constant.HttpConstant;
import com.mars.iserver.server.impl.MarsHttpExchange;
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
            MarsHttpHelper.read(channel, marsHttpExchange);
            MarsHttpHelper.write(marsHttpExchange);
        } catch (Exception e) {
            logger.error("处理请求异常", e);
            errorResponseText(e, marsHttpExchange);
        } finally {
            MarsHttpHelper.close(channel);
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
    private static void errorResponseText(Exception e, MarsHttpExchange marsHttpExchange){
        try {
            marsHttpExchange.setResponseHeader(MarsConstant.CONTENT_TYPE, HttpConstant.RESPONSE_CONTENT_TYPE);
            marsHttpExchange.responseText(MesUtil.getMes(500,"处理请求异常:" + e.getMessage()));
        } catch (Exception ex){
        }
    }
}
