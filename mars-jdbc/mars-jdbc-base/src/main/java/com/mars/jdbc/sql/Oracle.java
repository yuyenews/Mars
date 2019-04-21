package com.mars.jdbc.sql;

import com.mars.jdbc.base.BaseSql;

public class Oracle extends BaseSql {

    /**
     * 查询所有表名
     * @param dbName
     * @return
     */
    @Override
    public String getTableNames(String dbName) {
        return "select * from user_tab_comments";
    }

    /**
     * 查询所有列名
     * @param dbName
     * @param tableName
     * @return
     */
    @Override
    public String getTableCloums(String dbName, String tableName) {
        return "select  *  from user_col_comments  where Table_Name='"+tableName+"'";
    }
}
