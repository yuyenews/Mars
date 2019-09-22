package com.mars.mj.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsSelect {

    /**
     * sql语句
     * @return
     */
    String sql();
}
