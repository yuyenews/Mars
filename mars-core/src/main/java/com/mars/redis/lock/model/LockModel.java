package com.mars.redis.lock.model;

/**
 * 分布式锁的参数
 */
public class LockModel {

    /**
     * 锁的唯一表示
     */
    private String key;

    /**
     * 值，记录是谁加的
     */
    private String value;

    /**
     * 最大等待时间
     */
    private int maxWait;

    /**
     * 重试频率
     */
    private int retryRate;

    /**
     * 是否重试
     */
    private boolean retry;

    /**
     * 失效时间
     */
    private long timeOut;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getRetryRate() {
        return retryRate;
    }

    public void setRetryRate(int retryRate) {
        this.retryRate = retryRate;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
}
