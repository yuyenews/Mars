package com.mars.common.annotation.bean;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsTimer {

    /**
     * 循环间隔
     * @return 间隔
     */
    int loop() default 60000;
}
