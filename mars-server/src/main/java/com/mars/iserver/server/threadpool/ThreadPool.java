package com.mars.iserver.server.threadpool;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.base.config.model.ThreadPoolConfig;
import com.mars.common.util.MarsConfiguration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 执行请求的线程池
 * 3.1.1版本开始废弃了，暂时用不到了，未来不确定用不用
 */
@Deprecated
public class ThreadPool {

    /**
     * 线程池
     */
    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 获取线程池
     * 3.1.1版本开始废弃了，暂时用不到了，未来不确定用不用
     * @return
     */
    @Deprecated
    public static ThreadPoolExecutor getThreadPoolExecutor(){
        if(threadPoolExecutor == null){
            /* 获取线程池的配置 */
            MarsConfig marsConfig = MarsConfiguration.getConfig();
            ThreadPoolConfig threadPoolConfig = marsConfig.threadPoolConfig();
            int maxPoolSize = threadPoolConfig.getMaxPoolSize();
            int corePoolSize = threadPoolConfig.getCorePoolSize();
            int keepAliveTime = threadPoolConfig.getKeepAliveTime();

            /* 创建线程池 */
            threadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,
                    maxPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(maxPoolSize-corePoolSize));
        }
        return threadPoolExecutor;
    }
}
