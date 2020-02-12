package com.mars.core.base.config.model;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.util.List;

public class JedisConfig {

    private JedisPoolConfig jedisPoolConfig;

    private List<JedisShardInfo> jedisShardInfoList;

    public JedisPoolConfig getJedisPoolConfig() {
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
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public List<JedisShardInfo> getJedisShardInfoList() {
        return jedisShardInfoList;
    }

    public void setJedisShardInfoList(List<JedisShardInfo> jedisShardInfoList) {
        this.jedisShardInfoList = jedisShardInfoList;
    }
}
