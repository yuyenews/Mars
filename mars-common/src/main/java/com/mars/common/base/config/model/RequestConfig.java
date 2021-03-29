package com.mars.common.base.config.model;

/**
 * 请求配置
 */
public class RequestConfig {

    /**
     * 每次读取大小
     * 默认1M
     */
    private int readSize = 1 * 1024 * 1024;

    /**
     * 读取超时时间
     * 默认10秒
     */
    private long readTimeout = 10000;

    /**
     * 响应超时时间
     * 默认10秒
     */
    private long writeTimeout = 10000;

    public int getReadSize() {
        return readSize;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
