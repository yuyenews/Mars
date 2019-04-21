package com.mars.jdbc.base;

import java.util.List;

/**
 * sql语句
 */
public abstract class BaseSql {

    /**
     * 查询所有表名
     * @param dbName
     * @return
     */
    public abstract String getTableNames(String dbName);

    /**
     * 查询所有列名
     * @param dbName
     * @param tableName
     * @return
     */
    public abstract String getTableCloums(String dbName,String tableName);

    /**
     * 构建单表查询语句
     * @param cloumsName
     * @param tableName
     * @return
     */
    public String builderSelect(List<String> cloumsName, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        for(int i=0;i<cloumsName.size();i++){
            if(i != 0){
                sql.append(",");
            }
            sql.append(cloumsName.get(i));
        }
        sql.append(" from ");
        sql.append(tableName);
        return sql.toString();
    }

    /**
     * 构建单表删除语句
     * @param tableName
     * @param id
     * @return
     */
    public String builderDelete(String tableName, String key, String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(key);
        sql.append(" =");
        sql.append(id);
        return sql.toString();
    }

    /**
     * 构建单表修改语句
     * @param cloumsName
     * @param tableName
     * @param id
     * @return
     */
    public String builderUpdate(List<String> cloumsName, String tableName, String key, String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("update ");
        sql.append(tableName);
        sql.append(" set ");
        for(int i = 0;i <cloumsName.size(); i++){
            if(i != 0){
                sql.append(",");
            }
            sql.append(cloumsName.get(i));
            sql.append(" = {"+cloumsName.get(i)+"}");
        }
        return sql.toString();
    }

    /**
     * 构建单表插入语句
     * @param cloumsName
     * @param tableName
     * @return
     */
    public String builderInsert(List<String> cloumsName, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("(");
        for(int i = 0;i<cloumsName.size();i++){
            if(i != 0){
                sql.append(",");
            }
            sql.append(cloumsName.get(i));
        }
        sql.append(" )");
        sql.append("values(");
        for(int i = 0;i<cloumsName.size();i++){
            if(i != 0){
                sql.append(",");
            }
            sql.append("{"+cloumsName.get(i)+"}");
        }
        sql.append(" )");
        return sql.toString();
    }
}
