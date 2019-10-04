package com.mars.mj.annotation;

import java.lang.annotation.*;
import java.util.Map;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsSelect {

    /**
     * sql语句
     * @return
     */
    String sql();

    /**
     * 返回类型
     * @return
     */
    Class<?> resultType() default Map.class;

    /**
     * 是否分页
     * @return
     */
    boolean page() default false;
}
