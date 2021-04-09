package com.mars.aio.server.impl;

import com.mars.common.util.MarsConfiguration;
import com.mars.aio.server.MarsServer;
import com.mars.server.MartianServer;
import com.mars.aio.server.threadpool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            MartianServer.builder()
                    .bind(portNumber, backLog)
                    .threadPool(ThreadPool.getThreadPoolExecutor())
                    .handler(new MarsServerDefaultHandler())
                    .start();

        } catch (Exception e) {
            log.error("AIO发生异常", e);
        }
    }
}
