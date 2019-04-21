package com.mars.jdbc.enums;

/**
 * sql类型
 */
public enum SqlEnums {

    MYSQL("mysql"),ORACLE("oracle");

    private String code;

    SqlEnums(String code){
        this.code = code;
    }

    /**
     * 根据code获取枚举
     * @param code
     * @return
     */
    public SqlEnums getByCode(String code){
        for(SqlEnums sqlEnums : this.values()){
            if(sqlEnums.code.equals(code)){
                return sqlEnums;
            }
        }
        return MYSQL;
    }
}
