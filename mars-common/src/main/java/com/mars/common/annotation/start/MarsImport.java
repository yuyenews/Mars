package com.mars.common.annotation.start;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MarsImport {
    String[] packageName();
}
