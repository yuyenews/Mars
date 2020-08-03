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
        return backLog<1 ? 50:backLog;
    }

    public void setBackLog(int backLog) {
        this.backLog = backLog;
    }

    public int getMaxPoolSize() {
        return Math.max(maxPoolSize, 10);
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getCorePoolSize() {
        return Math.max(corePoolSize, 1);
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime<1?10:keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
