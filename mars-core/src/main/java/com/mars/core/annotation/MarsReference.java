package com.mars.core.annotation;

import com.mars.core.annotation.enums.RefType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsReference {

    String beanName() default "";

    String refName() default "";

    RefType refType() default RefType.METHOD;
}
