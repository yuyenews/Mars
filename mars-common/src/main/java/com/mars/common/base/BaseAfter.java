package com.mars.common.base;

/**
 * 框架启动后立刻执行的类 必须实现这个接口
 */
public interface BaseAfter {

    void after() throws Exception;
}
