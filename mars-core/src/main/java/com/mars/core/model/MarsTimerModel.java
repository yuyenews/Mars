package com.mars.core.model;

import com.mars.core.annotation.MarsTimer;

import java.lang.reflect.Method;

/**
 * 定时任务实体类
 */
public class MarsTimerModel {

    /**
     * method名称
     */
    private String methodName;

    /**
     * 要执行的方法
     */
    private Method method;

    /**
     * 定时任务注解
     */
    private MarsTimer marsTimer;

    /**
     * bean对象
     */
    private Object obj;

    /**
     * class对象
     */
    private Class<?> cls;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public MarsTimer getMarsTimer() {
        return marsTimer;
    }

    public void setMarsTimer(MarsTimer marsTimer) {
        this.marsTimer = marsTimer;
    }
}
