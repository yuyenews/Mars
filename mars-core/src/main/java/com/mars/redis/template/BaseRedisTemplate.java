package com.mars.redis.template;

import com.mars.common.util.SerializableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * redis模板基类
 * @param <K>
 * @param <V>
 */
public class BaseRedisTemplate<K, V> {

    private Logger logger = LoggerFactory.getLogger(BaseRedisTemplate.class);

    /**
     * 从连接池获取redis连接
     * @return
     */
    public ShardedJedis getShardedJedis() throws Exception {
        ShardedJedis jedis = null;
        try {
            jedis = JedisPoolFactory.getShardedJedisPool().getResource();
        } catch (Exception e) {
            throw new Exception("获取redis连接失败",e);
        }

        return jedis;
    }

    /**
     * 回收redis连接
     * @param jedis
     */
    public void recycleJedis(ShardedJedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                logger.error("回收Jedis连接异常" + jedis, e);
            }
        }
    }

    /**
     * 转化key
     * @param key
     * @return
     * @throws Exception
     */
    protected byte[] getSerKey(K key) throws Exception {
        return SerializableUtil.serialization(key);
    }

    /**
     * 转化value
     * @param value
     * @return
     * @throws Exception
     */
    protected byte[] getSerValue(V value) throws Exception {
        return SerializableUtil.serialization(value);
    }

    /**
     * 反序列化key
     * @param key
     * @return
     * @throws Exception
     */
    protected K getDeKey(byte[] key) throws Exception {
        if(key == null){
            return null;
        }
        return (K)SerializableUtil.deSerialization(key, Object.class);
    }

    /**
     * 反序列化值
     * @param value
     * @return
     * @throws Exception
     */
    protected V getDeValue(byte[] value) throws Exception {
        if(value == null){
            return null;
        }
        return (V)SerializableUtil.deSerialization(value, Object.class);
    }
}
