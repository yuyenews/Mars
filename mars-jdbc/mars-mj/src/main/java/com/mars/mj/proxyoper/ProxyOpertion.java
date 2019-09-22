package com.mars.mj.proxyoper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.mj.annotation.MarsGet;
import com.mars.mj.annotation.MarsSelect;
import com.mars.mj.annotation.MarsUpdate;
import com.mars.mj.helper.templete.JdbcTemplete;

import java.lang.reflect.Method;

/**
 * 代理操作数据库
 */
public class ProxyOpertion {

    /**
     * 根据主键查询一条数据
     * @param marsGet 注解
     * @param dataSourceName 数据源
     * @param method 方法
     * @param param 参数
     * @return 数据
     * @throws Exception 异常
     */
    public static Object get(MarsGet marsGet, String dataSourceName, Method method, Object param) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ");
        sql.append(marsGet.tableName());
        sql.append(" where ");
        sql.append(marsGet.primaryKey());
        sql.append(" = ?");

        return JdbcTemplete.get(dataSourceName).selectOne(sql.toString(),new Object[]{param},method.getReturnType());
    }

    /**
     * 根据sql查询数据
     * @param marsSelect 注解
     * @param dataSourceName 数据源
     * @param method 方法
     * @param param 参数
     * @return 数据
     * @throws Exception 异常
     */
    public static Object select(MarsSelect marsSelect, String dataSourceName, Method method, Object param) throws Exception {
        return JdbcTemplete.get(dataSourceName).selectList(marsSelect.sql(),param, method.getReturnType());
    }

    /**
     * 单表增删改
     * @param marsUpdate 注解
     * @param dataSourceName 数据源
     * @param param 参数
     * @return 数据
     * @throws Exception 异常
     */
    public static Object update(MarsUpdate marsUpdate, String dataSourceName, Object param) throws Exception {
        switch (marsUpdate.operType()){
            case DELETE:
                return doDelete(marsUpdate,dataSourceName,param);
            case UPDATE:
                return doUpdate(marsUpdate,dataSourceName,param);
            case INSERT:
                return doInsert(marsUpdate,dataSourceName,param);
            default:
                throw new Exception("没有指定operType");
        }
    }

    /**
     * 单表增删改
     * @param marsUpdate 注解
     * @param dataSourceName 数据源
     * @param param 参数
     * @return 数据
     * @throws Exception 异常
     */
    private static Object doDelete(MarsUpdate marsUpdate, String dataSourceName, Object param) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(marsUpdate.tableName());
        sql.append(builderWhere(marsUpdate));

        return JdbcTemplete.get(dataSourceName).update(sql.toString(),new Object[]{param});
    }

    /**
     * 单表增删改
     * @param marsUpdate 注解
     * @param dataSourceName 数据源
     * @param param 参数
     * @return 数据
     * @throws Exception 异常
     */
    private static Object doInsert(MarsUpdate marsUpdate, String dataSourceName, Object param) throws Exception {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(param));

        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(marsUpdate.tableName());
        sql.append("(");
        boolean isFirst = true;
        for(String key : jsonObject.keySet()){
            if(!isFirst){
                sql.append(",");
            }
            sql.append(key);
            isFirst = false;
        }
        sql.append(") values(");
        isFirst = true;
        for(String key : jsonObject.keySet()){
            if(!isFirst){
                sql.append(",");
            }
            sql.append("#{");
            sql.append(key);
            sql.append("}");
            isFirst = false;
        }
        sql.append(")");

        return JdbcTemplete.get(dataSourceName).update(sql.toString(),param);
    }

    /**
     * 单表增删改
     * @param marsUpdate 注解
     * @param dataSourceName 数据源
     * @param param 参数
     * @return 数据
     * @throws Exception 异常
     */
    private static Object doUpdate(MarsUpdate marsUpdate, String dataSourceName, Object param) throws Exception {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(param));
        StringBuffer sql = new StringBuffer();
        sql.append("update ");
        sql.append(marsUpdate.tableName());
        sql.append(" set ");
        boolean isFirst = true;
        for(String key : jsonObject.keySet()){
            if(!isFirst){
                sql.append(",");
            }
            sql.append(key);
            sql.append(" = #{");
            sql.append(key);
            sql.append("}");
            isFirst = false;
        }
        sql.append(builderWhere(marsUpdate));

        return JdbcTemplete.get(dataSourceName).update(sql.toString(),param);
    }

    /**
     * 构建条件
     * @param marsUpdate 注解
     * @return 条件
     */
    private static StringBuffer builderWhere(MarsUpdate marsUpdate){
        StringBuffer sql = new StringBuffer();
        sql.append(" where ");
        sql.append(marsUpdate.primaryKey());
        sql.append(" = #{");
        sql.append(marsUpdate.primaryKey());
        sql.append("}");
        return sql;
    }
}
