package com.mars.jdbc.helper.templete.model;

public class SqlBuilderModel {

    private String sql;

    private Object[] params;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
