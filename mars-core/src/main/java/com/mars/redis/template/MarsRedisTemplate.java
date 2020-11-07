package com.mars.redis.template;

import com.mars.common.annotation.bean.MarsBean;
import com.mars.redis.template.ops.Hash;
import com.mars.redis.template.ops.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * Redis操作
 */
@MarsBean
public class MarsRedisTemplate<K, V> extends BaseRedisTemplate<K, V> {

    private Logger logger = LoggerFactory.getLogger(MarsRedisTemplate.class);

    /**
     * 普通的key-value操作
     */
    private Values<K, V> values = new Values<>();

    /**
     * hash操作
     */
    private Hash<K, V> hash = new Hash<>();

    /**
     * 获取值操作
     *
     * @return
     */
    public Values<K, V> values() {
        return values;
    }

    /**
     * 获取hash操作
     *
     * @return
     */
    public Hash<K, V> hash() {
        return hash;
    }

    /**
     * 根据key删除数据
     *
     * @param key
     */
    public void del(K key) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.del(getSerKey(key));
        } catch (Exception e) {
            logger.error("删除异常，key:{}", key, e);
        } finally {
            recycleJedis(jedis);
        }
    }

    /**
     * 更新byte类型的数据，主要更新过期时间
     * @param key
     * @param expireTimeSecond
     */
    public void expire(K key, int expireTimeSecond) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.expire(getSerKey(key), expireTimeSecond);
        } catch (Exception e) {
            logger.error("expire 异常:" + key, e);
        } finally {
            recycleJedis(jedis);
        }
    }
}
