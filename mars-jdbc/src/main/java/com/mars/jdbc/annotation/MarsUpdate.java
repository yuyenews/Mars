package com.mars.jdbc.annotation;

import com.mars.jdbc.annotation.enums.OperType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsUpdate {

    /**
     * 表名
     * @return
     */
    String tableName();

    /**
     * 主键名称
     * @return
     */
    String primaryKey() default "";

    /**
     * 操作类型
     * @return
     */
    OperType operType();
}
