package com.mars.common.annotation.api;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsDataCheck {
    /**
     * 是否可为空
     * @return
     */
    boolean notNull() default false;

    /**
     * 最小长度
     * @return
     */
    long minLength() default 0;

    /**
     * 最大长度
     * @return
     */
    long maxLength() default Long.MAX_VALUE;

    /**
     * 正则
     * @return
     */
    String reg() default "";

    /**
     * 提示语
     * @return
     */
    String msg() default "请填写正确的数据";

    /**
     * 作用域
     * @return
     */
    String[] apis();
}
