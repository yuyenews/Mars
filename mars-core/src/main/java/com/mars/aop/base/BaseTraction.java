package com.mars.aop.base;

import com.mars.aop.model.AopModel;

/**
 * 事务管理
 */
public interface BaseTraction {

    /**
     * 获取数据库连接，并设置为不自动提交
     *
     * 将获取到的连接 放到缓存中
     *
     * @param aopModel aop对象
     */
    void beginTraction(AopModel aopModel);

    /**
     * 从缓存中获取当前线程的数据库连接，并提交事务
     *
     */
    void commit();

    /**
     * 从缓存中获取当前线程的数据库连接，并回滚事务
     * @param e 异常
     */
    void rollback(Throwable e);
}
