package com.mars.common.annotation.api;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsInterceptor {
    String pattern() default "*";
}
