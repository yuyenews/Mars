package com.mars.mj.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsGet {

    /**
     * 表名
     * @return
     */
    String tableName();

    /**
     * 主键名称
     * @return
     */
    String primaryKey();
}
