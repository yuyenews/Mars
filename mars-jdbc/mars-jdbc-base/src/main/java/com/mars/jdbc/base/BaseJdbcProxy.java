package com.mars.jdbc.base;

/**
 * JDBC代理
 */
public abstract class BaseJdbcProxy {

    /**
     * 获取代理对象
     * @param clazz  bean的class
     * @return 对象
     */
    public abstract Object getProxy(Class<?> clazz);
}
