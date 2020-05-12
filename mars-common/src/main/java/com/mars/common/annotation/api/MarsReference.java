package com.mars.common.annotation.api;

import com.mars.common.annotation.enums.RefType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsReference {

    String beanName() default "";

    String refName() default "";

    RefType refType() default RefType.METHOD;
}
