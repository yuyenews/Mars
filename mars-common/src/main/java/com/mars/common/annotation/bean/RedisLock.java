package com.mars.common.annotation.bean;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * 锁的唯一标识
     * @return
     */
    String key();

    /**
     * 最大等待时间，默认3秒
     */
    int maxWait() default 3000;

    /**
     * 重试频率，默认100毫秒
     */
    int retryRate() default 100;

    /**
     * 是否重试，默认不重试
     */
    boolean retry() default false;

    /**
     * 失效时间，默认10秒
     */
    long timeOut() default 10000;
}
