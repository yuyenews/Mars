package com.mars.aio.server.handler;

import com.mars.aio.server.impl.MarsHttpExchange;
import com.mars.common.base.config.model.RequestConfig;
import com.mars.common.util.MarsConfiguration;
import com.mars.aio.server.helper.MarsHttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

/**
 * 服务联络器
 */
public class MarsAioServerHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private Logger logger = LoggerFactory.getLogger(MarsAioServerHandler.class);

    /**
     * 请求配置
     */
    private RequestConfig requestConfig = MarsConfiguration.getConfig().requestConfig();

    /**
     * 处理请求
     * @param channel
     * @param serverSocketChannel
     */
    @Override
    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel serverSocketChannel) {
        /* 允许下一个请求进来 */
        serverSocketChannel.accept(serverSocketChannel, this);

        /* 创建请求对象 */
        MarsHttpExchange marsHttpExchange = new MarsHttpExchange();
        marsHttpExchange.setSocketChannel(channel);

        try {
            /* 开始读取数据 */
            ByteBuffer readBuffer = ByteBuffer.allocate(800);
            channel.read(readBuffer, requestConfig.getReadTimeout(), TimeUnit.MILLISECONDS,readBuffer, new MarsReadAndWriteHandler(marsHttpExchange));
         } catch (Exception e) {
            logger.error("处理请求异常", e);
            MarsHttpHelper.errorResponseText(e, marsHttpExchange);
            MarsHttpHelper.close(channel, true);
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
}
