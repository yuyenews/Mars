package com.mars.jdbc.sqlbuild.enums;

/**
 * join类型
 */
public enum JoinType {

    LEFT("left join"),RIGHT("right join");

    String code;

    JoinType(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
