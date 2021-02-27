package com.mars.common.base.config.model;

/**
 * 线程池配置
 */
public class ThreadPoolConfig {

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

    /**
     * 最大连接数
     */
    private int backLog = 2000;

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

    public int getBackLog() {
        return backLog;
    }

    public void setBackLog(int backLog) {
        this.backLog = backLog;
    }
}
