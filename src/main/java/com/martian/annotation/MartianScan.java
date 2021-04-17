package com.martian.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MartianScan {

    /**
     * 要扫描的包
     * @return
     */
    String[] scanPackage();
}
