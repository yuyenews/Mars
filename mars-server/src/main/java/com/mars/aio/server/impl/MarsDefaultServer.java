package com.mars.aio.server.impl;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.base.config.model.FileUploadConfig;
import com.mars.common.base.config.model.RequestConfig;
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

    private MarsConfig marsConfig = MarsConfiguration.getConfig();

    /**
     * 开启服务
     *
     * @param portNumber
     */
    @Override
    public void start(int portNumber) {
        try {
            int backLog = marsConfig.threadPoolConfig().getBackLog();
            FileUploadConfig fileUploadConfig = marsConfig.fileUploadConfig();
            RequestConfig requestConfig = marsConfig.requestConfig();

            /* 创建服务，并启动 */
            MartianServer.builder()
                    .bind(portNumber, backLog)
                    .fileSizeMax(fileUploadConfig.getFileSizeMax())
                    .sizeMax(fileUploadConfig.getSizeMax())
                    .readSize(requestConfig.getReadSize())
                    .readTimeout(requestConfig.getReadTimeout())
                    .writeTimeout(requestConfig.getWriteTimeout())
                    .threadPool(ThreadPool.getThreadPoolExecutor())
                    .httpHandler("/",new MarsServerDefaultHandler())
                    .start();

        } catch (Exception e) {
            log.error("启动服务发生异常", e);
        }
    }
}
