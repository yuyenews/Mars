package com.mars.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsTimer {

    /**
     * 循环间隔
     * @return
     */
    long loop();
}
