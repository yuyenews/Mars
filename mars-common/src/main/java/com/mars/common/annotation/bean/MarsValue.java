package com.mars.common.annotation.bean;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsValue {
    String value() default "";
}
