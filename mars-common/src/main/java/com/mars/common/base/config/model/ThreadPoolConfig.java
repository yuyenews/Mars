package com.mars.common.base.config.model;

/**
 * 线程池配置
 */
public class ThreadPoolConfig {

    /**
     * TCP最大连接池
     */
    private int backLog = 50;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 2000;

    /**
     * 核心线程数
     */
    private int corePoolSize = 5;

    /**
     * 最大等待时长，默认20秒
     */
    private int keepAliveTime = 20;

    public int getBackLog() {
        if(backLog < 1){
            return 50;
        }
        return backLog;
    }

    public void setBackLog(int backLog) {
        this.backLog = backLog;
    }

    public int getMaxPoolSize() {
        if(maxPoolSize < 10){
            return 10;
        }
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getCorePoolSize() {
        if(corePoolSize < 1){
            return 1;
        }
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getKeepAliveTime() {
        if (keepAliveTime < 1) {
            return 10;
        }
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
