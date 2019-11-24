package com.mars.mvc.model;

/**
 * 存储拦截器对象的实体类
 */
public class MarsInterModel {

    /**
     * 匹配规则
     */
    private String pattern;

    /**
     * 拦截器对象
     */
    private Object obj;

    /**
     * 拦截器class
     */
    private Class cls;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
