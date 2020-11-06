package com.mars.redis.template.ops;

import com.mars.redis.template.BaseRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * value操作
 * @param <K>
 * @param <V>
 */
public class Values<K, V> extends BaseRedisTemplate<K, V> {

    private Logger logger = LoggerFactory.getLogger(Values.class);

    /**
     * 设置redis值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setValue(K key, V value) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.set(getSerKey(key), getSerValue(value));
            return true;
        } catch (Exception e) {
            logger.error("添加 redis value 异常, key:{},value:{}" , key, value, e);
        } finally {
            recycleJedis(jedis);
        }
        return false;
    }

    /**
     * 设置redis值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setValueEx(K key, V value,Integer ex) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.setex(getSerKey(key), ex, getSerValue(value));
            return true;
        } catch (Exception e) {
            logger.error("添加 redis value 异常, key:{},value:{}" , key, value, e);
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
    public V getValue(K key) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            byte[] result = jedis.get(getSerKey(key));
            return getDeValue(result);
        } catch (Exception e) {
            logger.error("获取 redis value 异常, key:{}", key, e);
        } finally {
            recycleJedis(jedis);
        }
        return null;
    }
}
