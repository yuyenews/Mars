package com.mars.aop.proxy.exec;

import com.mars.core.annotation.RedisLock;
import com.mars.ioc.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 执行redis分布式锁
 *
 * 通过调用redis模块的MarsRedisLock类里面的方法，进行加解锁操作
 */
public class ExecRedisLock {

    private static Logger logger = LoggerFactory.getLogger(ExecRedisLock.class);

    /**
     * 分布式锁的class对象
     */
    private static Class<?> redisLockClass;

    /**
     * 分布式锁的实例
     */
    private static Object redisLockObj;

    /**
     * 加锁
     * @param redisLock 注解
     * @return 加锁结果
     * @throws Exception 异常
     */
    public static Boolean lock(RedisLock redisLock) {
        return exec(redisLock,"lock");
    }

    /**
     * 解锁
     * @param redisLock 注解
     * @return 解锁结果
     * @throws Exception 异常
     */
    public static Boolean unlock(RedisLock redisLock) {
        return exec(redisLock,"unlock");
    }

    /**
     * 执行加解锁操作
     * @param redisLock 注解
     * @param methodName 执行的方法
     * @return 结果
     * @throws Exception 异常
     */
    private static Boolean exec(RedisLock redisLock,String methodName) {
        try {
            if(redisLock == null){
                /* 这个true代表不需要加解锁，为了让程序继续往下走 */
                return true;
            }
            redisLockClass = getRedisLockClass();
            redisLockObj = getRedisLockObj();
            Method method = redisLockClass.getMethod(methodName,new Class[]{String.class});
            Object result = method.invoke(redisLockObj,new Object[]{redisLock.key()});
            if(result == null){
                return false;
            }
            return Boolean.parseBoolean(result.toString());
        } catch (Exception e){
            logger.error("分布式锁出现异常["+methodName+"]",e);
            return false;
        }
    }

    /**
     * 获取分布式锁的class对象
     * @return 分布式锁的class对象
     * @throws Exception 异常
     */
    private static Class<?> getRedisLockClass() throws Exception {
        /* 这里只是为了节约性能，在首次并发的情况下，即使执行了多次，也不会存在安全问题 */
        if(redisLockClass == null) {
            redisLockClass = Class.forName("com.mars.redis.lock.MarsRedisLock");
        }
        return redisLockClass;
    }

    /**
     * 获取分布式锁的实例对象
     * @return 分布式锁的实例对象
     * @throws Exception 异常
     */
    private static Object getRedisLockObj() throws Exception {
        /* 这里只是为了节约性能，在首次并发的情况下，即使执行了多次，也不会存在安全问题 */
        if(redisLockObj == null) {
            redisLockObj = BeanFactory.getBean("marsRedisLock",Object.class);
        }
        return redisLockObj;
    }
}
