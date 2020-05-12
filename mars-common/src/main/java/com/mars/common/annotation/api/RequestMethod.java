package com.mars.common.annotation.api;

import com.mars.common.annotation.enums.ReqMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMethod {

	ReqMethod value() default ReqMethod.GET;
}
