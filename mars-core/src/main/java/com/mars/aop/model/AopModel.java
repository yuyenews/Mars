package com.mars.aop.model;

import com.mars.common.annotation.enums.TractionLevel;

/**
 * Aop实体
 */
public class AopModel {

    /**
     * Aop执行的类
     */
    private Class cls;

    /**
     * Aop执行的对象
     */
    private Object obj;

    /**
     * 事务隔离级别
     */
    private TractionLevel tractionLevel;

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public TractionLevel getTractionLevel() {
        return tractionLevel;
    }

    public void setTractionLevel(TractionLevel tractionLevel) {
        this.tractionLevel = tractionLevel;
    }

}
