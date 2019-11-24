package com.mars.redis.template;

import com.mars.core.annotation.MarsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Redis操作
 */
@MarsBean
public class MarsRedisTemplate {

    private Logger logger = LoggerFactory.getLogger(MarsRedisTemplate.class);

    /**
     * 从连接池获取redis连接
     * @return
     */
    public ShardedJedis getShardedJedis() throws Exception {
        ShardedJedis jedis = null;
        try {
            jedis = JedisPoolFactory.getShardedJedisPool().getResource();
        } catch (Exception e) {
            throw new Exception("get Jedis error",e);
        }

        return jedis;
    }

    /**
     * 模糊查询keys
     *
     * @param pattern
     * @return
     */
    public TreeSet<String> keys(String pattern) {
        ShardedJedis jedis = null;
        TreeSet<String> allKeys = new TreeSet<>();

        try {
            jedis = getShardedJedis();
            Collection<Jedis> jedisList = jedis.getAllShards();// 获取所有的缓存实例
            for (Jedis jedisItem : jedisList) {
                allKeys.addAll(jedisItem.keys(pattern));// 获取匹配prefix的所有的key
            }
        } catch (Exception e) {
            logger.error("Getting keys error: {}", e);
        } finally {
            recycleJedis(jedis);
        }

        return allKeys;
    }

    /**
     * 设置redis值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value) {
        return set(key.getBytes(),value.getBytes());
    }

    /**
     * 设置redis值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(byte[] key, byte[] value) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("set redis value error:" + key + "@" + value, e);
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
    public boolean setex(byte[] key, byte[] value,Integer ex) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.setex(key,ex,value);
            return true;
        } catch (Exception e) {
            logger.error("set redis value error:" + key + "@" + value, e);
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
    public boolean setex(String key, String value,Integer ex) {
        return setex(key.getBytes(),value.getBytes(),ex);
    }

    /**
     * 获取redis值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        byte[] result = get(key.getBytes());
        return new String(result);
    }

    /**
     * 获取redis值
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("get redis value error:" + key, e);
        } finally {
            recycleJedis(jedis);
        }

        return null;
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
     * 根据key删除数据
     * @param key
     */
    public void del(String key) {
        del(key.getBytes());
    }

    /**
     * 根据key删除数据
     * @param key
     */
    public void del(byte[] key) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.del(key);
        } catch (Exception e) {
            logger.error("delString error:" + key, e);
        } finally {
            recycleJedis(jedis);
        }
    }

    /**
     * 更新byte类型的数据，主要更新过期时间
     * @param key
     * @param expireTimeSecond
     */
    public void updateObjectEnableTime(byte[] key, int expireTimeSecond) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.expire(key, expireTimeSecond);
        } catch (Exception e) {
            logger.error("updateObject error:" + key, e);
        } finally {
            recycleJedis(jedis);
        }
    }

    /**
     * 更新String类型的数据，主要更新过期时间
     * @param key
     * @param expireTimeSecond
     */
    public void updateObjectEnableTime(String key, int expireTimeSecond) {
        updateObjectEnableTime(key.getBytes(),expireTimeSecond);
    }

    /**
     * 获取所有keys
     * @param pattern
     * @return
     */
    public Set<String> getAllKeys(String pattern) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            return jedis.hkeys(pattern);
        } catch (Exception e) {
            logger.error("getAllKeys error:" + pattern, e);
        } finally {
            recycleJedis(jedis);
        }
        return null;
    }



    /**
     * 设置值
     * @param group
     * @param key
     * @param value
     * @return
     */
    public boolean hset(String group,String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = getShardedJedis();
            jedis.hset(group,key, value);
            return true;
        } catch (Exception e) {
            logger.error("set redis value error:" + key + "@" + value, e);
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
    public String hget(String group, String key){
        ShardedJedis jedis = null;
        String result = "";
        try {
            jedis = getShardedJedis();
            result = jedis.hget(group, key);
        } catch (Exception e) {
            logger.error("hget redis value error:" + group + "@" + key, e);
        } finally {
            recycleJedis(jedis);
        }

        return result;
    }

    /**
     * 获取一组数据
     * @param group
     * @return
     */
    public Map<String, String> hgetAll(String group){
        ShardedJedis jedis = null;
        Map<String, String>  result = null;
        try {
            jedis = getShardedJedis();
            result = jedis.hgetAll(group);
        } catch (Exception e) {
            logger.error("hgetAll redis value error:" + group, e);
        } finally {
            recycleJedis(jedis);
        }

        return result;
    }
}
