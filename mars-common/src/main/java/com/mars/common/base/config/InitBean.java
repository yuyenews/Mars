package com.mars.common.base.config;

/**
 * 当需要对一个bean进行初始化的时候，实现这个接口
 */
public interface InitBean {

    /**
     * 并实现这个方法
     */
    void init();
}
