package com.mars.common.annotation.api;

import com.mars.server.http.constant.ReqMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMethod {

	ReqMethod[] value() default {ReqMethod.GET, ReqMethod.POST, ReqMethod.DELETE, ReqMethod.PUT};
}
