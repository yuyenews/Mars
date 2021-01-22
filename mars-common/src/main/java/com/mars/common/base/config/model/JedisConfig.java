package com.mars.common.base.config.model;

import redis.clients.jedis.JedisPoolConfig;

public class JedisConfig {

    /**
     * 用哪个库
     */
    private int database = 0;

    /**
     * redis服务ip
     */
    private String host;

    /**
     * redis服务的端口号
     */
    private int port;

    /**
     * 是否是ssl
     */
    private boolean ssl = false;

    /**
     * 超时时间
     */
    private int timeout = 5000;

    /**
     * 用户
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    private JedisPoolConfig jedisPoolConfig;

    public JedisPoolConfig getJedisPoolConfig() {
        if(jedisPoolConfig == null){
            jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(2048);
            jedisPoolConfig.setMaxIdle(200);
            jedisPoolConfig.setMinIdle(2);
            jedisPoolConfig.setNumTestsPerEvictionRun(2048);
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
            jedisPoolConfig.setMinEvictableIdleTimeMillis(-1);
            jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(10000);
            jedisPoolConfig.setMaxWaitMillis(10000);
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPoolConfig.setTestWhileIdle(true);
            jedisPoolConfig.setTestOnReturn(true);
            jedisPoolConfig.setJmxEnabled(true);
            jedisPoolConfig.setBlockWhenExhausted(true);
        }
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
