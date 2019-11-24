package com.mars.redis.lock;

import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.MarsWrite;
import com.mars.redis.template.MarsRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.params.SetParams;

/**
 * redis锁
 */
@MarsBean
public class MarsRedisLock {

    private Logger logger = LoggerFactory.getLogger(MarsRedisLock.class);

    @MarsWrite
    private MarsRedisTemplate marsRedisTemplate;


    /**
     * 加锁，使用框架上配置的redis
     * @param key 键
     * @return
     */
    public boolean lock(String key){
        try {
            ShardedJedis shardedJedis = marsRedisTemplate.getShardedJedis();
            return lock(key ,shardedJedis);
        } catch (Exception e){
            logger.error("获取redis锁发生异常",e);
            return false;
        }
    }

    /**
     * 加锁，使用你自己创建的jedis对象
     * @param key 键
     * @param shardedJedis 自己创建的jedis对象
     * @return
     */
    public boolean lock(String key, ShardedJedis shardedJedis){
        try {
            if(shardedJedis == null){
                return false;
            }
            int count = 0;
            String value = "lock";

            SetParams params = SetParams.setParams().nx().px(20000);
            String result = shardedJedis.set(key,value,params);

            while(result == null || !result.toUpperCase().equals("OK")){

                /* 如果设置失败，代表这个key已经存在了,也就说明锁被占用了，则进入等待 */
                Thread.sleep(2000);
                if(count >= 9){
                    /* 20秒后还没有获取锁，则停止等待 */
                    return false;
                }
                result = shardedJedis.set(key,value,params);

                count++;
            }
            return true;
        } catch (Exception e){
            logger.error("获取redis锁发生异常",e);
            return false;
        } finally {
            marsRedisTemplate.recycleJedis(shardedJedis);
        }
    }

    /**
     * 释放锁，使用框架上配置的redis
     * @param key 键
     * @return
     */
    public boolean unlock(String key){
        try {
            ShardedJedis shardedJedis = marsRedisTemplate.getShardedJedis();
            return unlock(key ,shardedJedis);
        } catch (Exception e){
            logger.error("释放redis锁发生异常",e);
            return false;
        }
    }

    /**
     * 释放锁，使用你自己创建的jedis对象
     * @param key 键
     * @param shardedJedis 自己创建的jedis对象
     * @return
     */
    public boolean unlock(String key, ShardedJedis shardedJedis){
        try {
            if(shardedJedis == null){
                return false;
            }
            shardedJedis.del(key);
            return true;
        } catch (Exception e) {
            logger.error("释放redis锁发生异常", e);
            return false;
        } finally {
            marsRedisTemplate.recycleJedis(shardedJedis);
        }
    }
}
