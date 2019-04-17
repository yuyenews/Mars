package com.mars.core.annotation;

import java.lang.annotation.*;

/**
 * 加在controller的方法上 表示打印日志
 * @author yuye
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsLog {

}
