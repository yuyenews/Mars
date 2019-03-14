package com.yuyenews.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyInterceptor {
    String pattern() default "";
}
