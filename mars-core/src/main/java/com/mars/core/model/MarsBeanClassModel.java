package com.mars.core.model;

/**
 * 储存扫描出来的类
 */
public class MarsBeanClassModel {

    private Class<?> className;

    private Object annotation;

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }

    public Object getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Object annotation) {
        this.annotation = annotation;
    }
}
