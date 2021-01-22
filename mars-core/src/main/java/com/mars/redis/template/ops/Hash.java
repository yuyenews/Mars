package com.mars.redis.template.ops;

import com.mars.redis.template.BaseRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * hash操作
 * @param <K>
 * @param <V>
 */
public class Hash<K, V> extends BaseRedisTemplate<K, V> {

    private Logger logger = LoggerFactory.getLogger(Hash.class);

    /**
     * 设置值
     * @param group
     * @param key
     * @param value
     * @return
     */
    public boolean setHash(String group,K key, V value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hset(group.getBytes(),getSerKey(key), getSerValue(value));
            return true;
        } catch (Exception e) {
            logger.error("添加 redis hashValue 异常, key:{}, value:{}" ,key ,value, e);
        } finally {
            recycleJedis(jedis);
        }
        return false;
    }

    /**
     * 获取单个值
     * @param group
     * @param key
     * @return
     */
    public V getHash(String group, K key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            byte[] result = jedis.hget(group.getBytes(), getSerKey(key));
            return getDeValue(result);
        } catch (Exception e) {
            logger.error("getHash 异常, group:{}, key:{}", group, key, e);
        } finally {
            recycleJedis(jedis);
        }
        return null;
    }

    /**
     * 删除
     * @param group
     * @param key
     * @return
     */
    public boolean delHash(String group, K key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hdel(group.getBytes(), getSerKey(key));
            return true;
        } catch (Exception e) {
            logger.error("delHash 异常, group:{}, key:{}", group, key, e);
        } finally {
            recycleJedis(jedis);
        }
        return false;
    }

    /**
     * 获取一组数据
     * @param group
     * @return
     */
    public Map<K, V> getAllHash(String group){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Map<byte[], byte[]> result = jedis.hgetAll(group.getBytes());
            Map<K, V> deResult = new HashMap<>();
            for(Map.Entry<byte[], byte[]> entry : result.entrySet()){
                deResult.put(getDeKey(entry.getKey()), getDeValue(entry.getValue()));
            }
            return deResult;
        } catch (Exception e) {
            logger.error("getAllHash redis value 异常, group:{}", group, e);
        } finally {
            recycleJedis(jedis);
        }
        return null;
    }
}
