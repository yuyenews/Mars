package com.mars.redis.template;

import com.mars.common.util.SerializableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

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
                logger.error("recycleJedis error" + jedis, e);
            }
        }
    }

    /**
     * 转化key
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] getSerKey(K key) throws Exception {
        return SerializableUtil.serialization(key);
    }

    /**
     * 转化value
     * @param value
     * @return
     * @throws Exception
     */
    public byte[] getSerValue(V value) throws Exception {
        return SerializableUtil.serialization(value);
    }

    /**
     * 反序列化key
     * @param value
     * @return
     * @throws Exception
     */
    public K getDeKey(byte[] value) throws Exception {
        return (K)SerializableUtil.deSerialization(value, Object.class);
    }

    /**
     * 反序列化值
     * @param value
     * @return
     * @throws Exception
     */
    public V getDeValue(byte[] value) throws Exception {
        return (V)SerializableUtil.deSerialization(value, Object.class);
    }
}
