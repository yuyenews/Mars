package com.mars.common.annotation.jdbc;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsDao {
    String value() default "";
}
