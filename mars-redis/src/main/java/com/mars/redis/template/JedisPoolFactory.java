package com.mars.redis.template;

import com.mars.core.base.config.MarsConfig;
import com.mars.core.util.MarsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

/**
 * jedisPool工厂，用于获取JedisPool对象
 */
public class JedisPoolFactory {

    private static  Logger logger = LoggerFactory.getLogger(JedisPoolFactory.class);

    private static JedisPoolConfig jedisPoolConfig;

    private static ShardedJedisPool shardedJedisPool;

    /**
     * 获取ShardedJedisPool对象
     * @return
     */
    protected static ShardedJedisPool getShardedJedisPool() throws Exception {
        try{
            if(shardedJedisPool == null){
                initJedisPoolConfig();
                shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,getJedisShardInfoList());
            }

            return shardedJedisPool;
        } catch (Exception e) {
            logger.error("获取JedisPool对象出错",e);
            throw e;
        }
    }

    /**
     * 初始化JedisPoolConfig
     */
    private static void initJedisPoolConfig(){
        if(jedisPoolConfig == null){
            MarsConfig marsConfig = MarsConfiguration.getConfig();
            jedisPoolConfig = marsConfig.jedisConfig().getJedisPoolConfig();
        }
    }

    /**
     * 获取JedisShardInfoList
     * @return
     */
    private static List<JedisShardInfo>  getJedisShardInfoList() throws Exception {
        MarsConfig marsConfig = MarsConfiguration.getConfig();
        return marsConfig.jedisConfig().getJedisShardInfoList();
    }
}
