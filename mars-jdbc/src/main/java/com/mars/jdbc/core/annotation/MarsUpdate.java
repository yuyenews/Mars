package com.mars.jdbc.core.annotation;

import com.mars.jdbc.core.annotation.enums.OperType;

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

    /**
     * 条件
     * @return
     */
    String where() default "";
}
