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
    private int readTimeout = 10000;

    public int getReadSize() {
        return readSize;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
