package com.mars.aio.server.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.MarsConfiguration;
import com.mars.aio.server.MarsServer;
import com.mars.aio.server.threadpool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.*;

/**
 * 默认服务（采用AIO）
 */
public class MarsDefaultServer implements MarsServer {

    private Logger log = LoggerFactory.getLogger(MarsDefaultServer.class);

    /**
     * 开启服务
     *
     * @param portNumber
     */
    @Override
    public void start(int portNumber) {
        try {
            /* 获取配置的最大连接数 */
            int backLog = MarsConfiguration.getConfig().threadPoolConfig().getBackLog();

            /* 创建线程组 */
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(ThreadPool.getThreadPoolExecutor());
            /* 创建服务器通道 */
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            /* 开始监听端口 */
            serverSocketChannel.bind(new InetSocketAddress(portNumber), backLog);
            /* 添加handler */
            serverSocketChannel.accept(serverSocketChannel, new MarsAioServerHandler());

            /* 标识服务是否已经启动 */
            MarsSpace.getEasySpace().setAttr(MarsConstant.HAS_SERVER_START, "yes");
            log.info("启动成功");
        } catch (Exception e) {
            log.error("NIO发生异常", e);
        }
    }
}
