package com.mars.aop.proxy.exec;

import com.mars.common.annotation.bean.RedisLock;
import com.mars.common.util.StringUtil;
import com.mars.ioc.factory.BeanFactory;
import com.mars.redis.lock.MarsRedisLock;
import com.mars.redis.lock.model.LockModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行redis分布式锁
 *
 * 通过调用redis模块的MarsRedisLock类里面的方法，进行加解锁操作
 */
public class ExecRedisLock {

    private static Logger logger = LoggerFactory.getLogger(ExecRedisLock.class);

    private static final String LOCK = "lock";

    private static final String UN_LOCK = "unlock";

    /**
     * 分布式锁的实例
     */
    private static MarsRedisLock redisLockObj;

    /**
     * 加锁
     * @param redisLock
     * @param value
     * @return
     */
    public static Boolean lock(RedisLock redisLock, String value) {
        return exec(redisLock, value, LOCK);
    }

    /**
     * 解锁
     * @param redisLock
     * @param value
     * @return
     */
    public static Boolean unlock(RedisLock redisLock, String value) {
        return exec(redisLock, value, UN_LOCK);
    }

    /**
     * 执行加解锁操作
     * @param redisLock
     * @param value
     * @param methodName
     * @return
     */
    private static Boolean exec(RedisLock redisLock, String value, String methodName) {
        try {
            if (redisLock == null) {
                /* 这个true代表不需要加解锁，为了让程序继续往下走 */
                return true;
            }

            LockModel lockModel = new LockModel();
            lockModel.setKey(redisLock.key());
            lockModel.setValue(value);
            lockModel.setMaxWait(redisLock.maxWait());
            lockModel.setRetry(redisLock.retry());
            lockModel.setRetryRate(redisLock.retryRate());
            lockModel.setTimeOut(redisLock.timeOut());

            redisLockObj = getRedisLockObj();

            boolean result = false;
            if (methodName.equals(LOCK)) {
                result = redisLockObj.lock(lockModel);
            } else if (methodName.equals(UN_LOCK)) {
                result = redisLockObj.unlock(lockModel.getKey(), lockModel.getValue());
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
            String beanName = StringUtil.getFirstLowerCase(MarsRedisLock.class.getSimpleName());
            redisLockObj = BeanFactory.getBean(beanName, MarsRedisLock.class);
        }
        return redisLockObj;
    }
}
