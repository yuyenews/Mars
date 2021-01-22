package com.mars.redis.template;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.base.config.model.JedisConfig;
import com.mars.common.util.MarsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

/**
 * jedisPool工厂，用于获取JedisPool对象
 */
public class JedisPoolFactory {

    private static  Logger logger = LoggerFactory.getLogger(JedisPoolFactory.class);

    private static JedisPoolConfig jedisPoolConfig;

    private static JedisPool jedisPool;

    /**
     * 获取getJedis对象
     * @return
     */
    protected static Jedis getJedis() throws Exception {
        try {
            if (jedisPool == null) {
                initJedisPoolConfig();

                MarsConfig marsConfig = MarsConfiguration.getConfig();
                JedisConfig jedisConfig = marsConfig.jedisConfig();

                jedisPool = new JedisPool(jedisPoolConfig, jedisConfig.getHost(),
                        jedisConfig.getPort(), jedisConfig.getTimeout(),
                        jedisConfig.getUser(), jedisConfig.getPassword(),
                        jedisConfig.getDatabase(),jedisConfig.isSsl());
            }

            Jedis jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e) {
            logger.error("获取JedisPool对象出错", e);
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
}
