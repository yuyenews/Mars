package com.mars.netty.thread;

import com.mars.core.logger.MarsLogger;
import com.mars.netty.util.ResponseUtil;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 请求过多的拒绝策略
 */
public class MarsRejectedExecutionHandler implements RejectedExecutionHandler {

    private MarsLogger marsLogger = MarsLogger.getLogger(MarsRejectedExecutionHandler.class);

    /**
     * 拒绝策略
     * @param r
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        marsLogger.info("拒绝策略开始执行");
        if(r instanceof RequestThread){
            RequestThread requestThread = (RequestThread)r;
            ResponseUtil.sendForBidden(requestThread.getCtx(),"当前请求太多，请稍后访问");
            marsLogger.info("拒绝策略执行结束");
            return;
        }
        marsLogger.info("拒绝策略没有成功执行");
    }
}
