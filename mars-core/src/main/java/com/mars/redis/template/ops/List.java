package com.mars.redis.template.ops;

import com.mars.redis.template.BaseRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * list操作
 * @param <K>
 * @param <V>
 */
public class List<K, V> extends BaseRedisTemplate<K, V> {

    private Logger logger = LoggerFactory.getLogger(List.class);


    /**
     * 设置redis值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setList(K key, V value, long index) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.lset(getSerKey(key), index, getSerValue(value));
            return true;
        } catch (Exception e) {
            logger.error("添加 redis value 异常, key:{}, value:{}" , key, value, e);
        } finally {
            recycleJedis(jedis);
        }
        return false;
    }

    /**
     * 获取redis值
     *
     * @param key
     * @return
     */
    public V getIndex(K key, long index) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            byte[] result = jedis.lindex(getSerKey(key), index);
            return getDeValue(result);
        } catch (Exception e) {
            logger.error("获取 redis value 异常, key:{}", key, e);
        } finally {
            recycleJedis(jedis);
        }
        return null;
    }

    /**
     * 获取总长度
     * @param key
     * @return
     */
    public long getLength(K key){
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            return jedis.llen(getSerKey(key));
        } catch (Exception e) {
            logger.error("获取长度 异常, key:{}", key, e);
        } finally {
            recycleJedis(jedis);
        }
        return 0;
    }
}
