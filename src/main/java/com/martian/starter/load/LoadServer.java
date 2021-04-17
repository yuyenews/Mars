package com.martian.starter.load;

import com.martian.cache.MartianConfigCache;
import com.martian.config.MartianConfig;
import com.martian.config.model.CrossDomainConfig;
import com.martian.config.model.FileUploadConfig;
import com.martian.config.model.RequestConfig;
import com.martian.config.model.ThreadPoolConfig;
import com.martian.thread.ThreadPool;
import io.magician.Magician;

/**
 * 加载服务
 */
public class LoadServer {

    /**
     * 开始加载
     * @throws Exception
     */
    public static void load() throws Exception {

        /* 获取配置 */
        MartianConfig martianConfig = MartianConfigCache.getMartianConfig();

        ThreadPoolConfig threadPoolConfig = martianConfig.threadPoolConfig();
        FileUploadConfig fileUploadConfig = martianConfig.fileUploadConfig();
        RequestConfig requestConfig = martianConfig.requestConfig();
        CrossDomainConfig crossDomainConfig = martianConfig.crossDomainConfig();


        /* 创建服务 */
        Magician.createHttpServer()
                .bind(martianConfig.port(), threadPoolConfig.getBackLog())
                .fileSizeMax(fileUploadConfig.getFileSizeMax())
                .sizeMax(fileUploadConfig.getSizeMax())
                .readSize(requestConfig.getReadSize())
                .readTimeout(requestConfig.getReadTimeout())
                .writeTimeout(requestConfig.getWriteTimeout())
                .threadPool(ThreadPool.getThreadPoolExecutor())
                .httpHandler("/", req -> {

                    req.getResponse().setResponseHeader("Access-Control-Allow-Origin", crossDomainConfig.getOrigin());
                    req.getResponse().setResponseHeader("Access-Control-Allow-Methods", crossDomainConfig.getMethods());
                    req.getResponse().setResponseHeader("Access-Control-Max-Age", crossDomainConfig.getMaxAge());
                    req.getResponse().setResponseHeader("Access-Control-Allow-Headers", crossDomainConfig.getHeaders());
                    req.getResponse().setResponseHeader("Access-Control-Allow-Credentials", crossDomainConfig.getCredentials());

                    LoadWeb.getMagicianWeb().request(req);
                }).start();
    }
}
