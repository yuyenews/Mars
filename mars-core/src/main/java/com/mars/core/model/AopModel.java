package com.mars.core.model;

import com.mars.core.enums.ExecutorType;
import com.mars.core.enums.TractionLevel;

/**
 * Aop实体
 */
public class AopModel {

    /**
     * Aop执行的类
     */
    private Class cls;

    /**
     * 事务隔离级别
     */
    private TractionLevel tractionLevel;

    /**
     * 执行类型，mybatis专用
     */
    private ExecutorType executorType;

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public TractionLevel getTractionLevel() {
        return tractionLevel;
    }

    public void setTractionLevel(TractionLevel tractionLevel) {
        this.tractionLevel = tractionLevel;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }
}
