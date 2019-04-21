package com.mars.jdbc.factory;

import com.mars.jdbc.base.BaseSql;
import com.mars.jdbc.enums.SqlEnums;
import com.mars.jdbc.sql.MySql;
import com.mars.jdbc.sql.Oracle;

/**
 * sql工厂
 */
public class SqlFactory {

    /**
     * 获取sql对象
     * @param sqlEnums
     * @return
     */
    public static BaseSql getSqlObject(SqlEnums sqlEnums){
        switch (sqlEnums){
            case MYSQL:
                return new MySql();
            case ORACLE:
                return  new Oracle();
        }
        return null;
    }
}
