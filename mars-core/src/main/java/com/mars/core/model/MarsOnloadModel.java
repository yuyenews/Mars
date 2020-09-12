package com.mars.core.model;

/**
 * marsOnLoad的实体类
 */
public class MarsOnloadModel {

    /**
     * bean对象
     */
    private Object obj;

    /**
     * class对象
     */
    private Class<?> cls;

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
}
