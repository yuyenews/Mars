package com.mars.aop.proxy.exec;

import com.mars.core.annotation.RedisLock;
import com.mars.ioc.factory.BeanFactory;
import com.mars.redis.lock.MarsRedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行redis分布式锁
 *
 * 通过调用redis模块的MarsRedisLock类里面的方法，进行加解锁操作
 */
public class ExecRedisLock {

    private static Logger logger = LoggerFactory.getLogger(ExecRedisLock.class);

    /**
     * 分布式锁的实例
     */
    private static MarsRedisLock redisLockObj;

    /**
     * 加锁
     *
     * @param redisLock 注解
     * @param value     值
     * @return 加锁结果
     */
    public static Boolean lock(RedisLock redisLock, String value) {
        return exec(redisLock, value, "lock");
    }

    /**
     * 解锁
     *
     * @param redisLock 注解
     * @param value     值
     * @return 解锁结果
     */
    public static Boolean unlock(RedisLock redisLock, String value) {
        return exec(redisLock, value, "unlock");
    }

    /**
     * 执行加解锁操作
     *
     * @param redisLock  注解
     * @param value      值
     * @param methodName 执行的方法
     * @return 结果
     */
    private static Boolean exec(RedisLock redisLock, String value, String methodName) {
        try {
            if (redisLock == null) {
                /* 这个true代表不需要加解锁，为了让程序继续往下走 */
                return true;
            }
            redisLockObj = getRedisLockObj();

            boolean result = false;
            if(methodName.equals("lock")){
                result = redisLockObj.lock(redisLock.key(),value);
            } else if(methodName.equals("unlock")){
                result = redisLockObj.unlock(redisLock.key(),value);
            }
            return result;
        } catch (Exception e) {
            logger.error("分布式锁出现异常[" + methodName + "]", e);
            return false;
        }
    }

    /**
     * 获取分布式锁的实例对象
     *
     * @return 分布式锁的实例对象
     * @throws Exception 异常
     */
    private static MarsRedisLock getRedisLockObj() throws Exception {
        /* 这里只是为了节约性能，在首次并发的情况下，即使执行了多次，也不会存在安全问题 */
        if (redisLockObj == null) {
            redisLockObj = BeanFactory.getBean("marsRedisLock", MarsRedisLock.class);
        }
        return redisLockObj;
    }
}
