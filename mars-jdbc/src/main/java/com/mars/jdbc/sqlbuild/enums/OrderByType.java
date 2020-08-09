package com.mars.jdbc.sqlbuild.enums;

/**
 * 排序类型
 */
public enum OrderByType {

    ASC("asc"),DESC("desc");

    String code;

    OrderByType(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
