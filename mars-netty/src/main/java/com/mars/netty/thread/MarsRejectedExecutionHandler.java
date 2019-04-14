package com.mars.netty.thread;

import com.mars.netty.util.ResponseUtil;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 请求过多的拒绝策略
 */
public class MarsRejectedExecutionHandler implements RejectedExecutionHandler {

    /**
     * 拒绝策略
     * @param r
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if(r instanceof RequestThread){
            RequestThread requestThread = (RequestThread)r;
            ResponseUtil.sendForBidden(requestThread.getCtx(),"当前请求太多，请稍后访问");
        }
    }
}
