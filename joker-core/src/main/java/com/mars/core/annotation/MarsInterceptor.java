package com.mars.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsInterceptor {
    String pattern() default "*";
}
