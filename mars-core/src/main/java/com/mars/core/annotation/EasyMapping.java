package com.mars.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mars.core.annotation.enums.RequestMetohd;

/**
 * 映射控制层方法的注解 
 * @author yuye
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyMapping {
		
	    String value() default "";  
	    RequestMetohd method() default RequestMetohd.GET;  
}
