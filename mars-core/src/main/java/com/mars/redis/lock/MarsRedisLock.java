package com.mars.redis.lock;

import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.bean.MarsWrite;
import com.mars.redis.lock.model.LockModel;
import com.mars.redis.template.MarsRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.params.SetParams;

import java.util.Date;

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
     *
     * @param lockModel
     * @return
     */
    public boolean lock(LockModel lockModel) {
        try {
            ShardedJedis shardedJedis = marsRedisTemplate.getShardedJedis();
            return lock(lockModel, shardedJedis);
        } catch (Exception e) {
            logger.error("获取redis锁发生异常", e);
            return false;
        }
    }

    /**
     * 加锁，使用你自己创建的jedis对象
     *
     * @param lockModel
     * @param shardedJedis 自己创建的jedis对象
     * @return
     */
    public boolean lock(LockModel lockModel, ShardedJedis shardedJedis) {
        try {
            if (shardedJedis == null) {
                return false;
            }

            /* 这个时间，用来给后面计算等待了多少时间的 */
            Date nowDate = new Date();

            SetParams params = SetParams.setParams().nx().px(lockModel.getTimeOut());
            String result = shardedJedis.set(lockModel.getKey(), lockModel.getValue(), params);

            /* 如果加锁成功，则直接返回 */
            if(isLockSuccess(result)){
                return true;
            }

            /* 如果加锁失败，并且不重试，则直接返回失败 */
            if(!lockModel.isRetry()){
                return false;
            }

            while (true) {
                /* 如果设置失败，代表这个key已经存在了,也就说明锁被占用了，则进入等待 */
                Thread.sleep(lockModel.getRetryRate());

                /* 发起重试 */
                result = shardedJedis.set(lockModel.getKey(), lockModel.getValue(), params);
                if(isLockSuccess(result)){
                    /* 如果重试成功了，则返回加锁成功 */
                    return true;
                }

                /* 判断等待时间是否超过 最大等待时间了，如果超过了，就直接返回失败 */
                long waitTime = new Date().getTime() - nowDate.getTime();
                if (waitTime >= lockModel.getMaxWait()) {
                    /* 停止等待 */
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("获取redis锁发生异常", e);
            return false;
        } finally {
            marsRedisTemplate.recycleJedis(shardedJedis);
        }
    }

    /**
     * 释放锁，使用框架上配置的redis
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean unlock(String key, String value) {
        try {
            ShardedJedis shardedJedis = marsRedisTemplate.getShardedJedis();
            return unlock(key, value, shardedJedis);
        } catch (Exception e) {
            logger.error("释放redis锁发生异常", e);
            return false;
        }
    }

    /**
     * 释放锁，使用你自己创建的jedis对象
     *
     * @param key          键
     * @param value        值
     * @param shardedJedis 自己创建的jedis对象
     * @return
     */
    public boolean unlock(String key, String value, ShardedJedis shardedJedis) {
        try {
            if (shardedJedis == null) {
                return false;
            }
            String val = shardedJedis.get(key);
            if (val != null && val.equals(value)) {
                shardedJedis.del(key);
            }
            return true;
        } catch (Exception e) {
            logger.error("释放redis锁发生异常", e);
            return false;
        } finally {
            marsRedisTemplate.recycleJedis(shardedJedis);
        }
    }

    /**
     * 加锁是否成功
     * @param result
     * @return
     */
    private boolean isLockSuccess(String result){
        if(result == null){
            return false;
        } else if(result.equals("1") || result.toUpperCase().equals("OK")){
            return true;
        }
        return false;
    }
}
