package com.mars.jdbc.sql;

import com.mars.jdbc.base.BaseSql;

public class MySql extends BaseSql {

    /**
     * 查询所有表名
     * @param dbName
     * @return
     */
    @Override
    public String getTableNames(String dbName) {
        return "select table_name from information_schema.tables where table_schema='"+dbName+"' and table_type='base table'";
    }

    /**
     * 查询所有列名
     * @param dbName
     * @param tableName
     * @return
     */
    @Override
    public String getTableCloums(String dbName,String tableName) {
        return "select column_name from information_schema.columns where table_schema='"+dbName+"' and table_name='"+tableName+"'";
    }
}
