package com.mars.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mars.core.annotation.enums.RequestMetohd;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsMapping {
		
	    String value() default "";  
	    RequestMetohd method() default RequestMetohd.GET;  
}
