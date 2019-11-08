package com.mars.jdbc.helper.templete;

import com.mars.jdbc.helper.templete.base.BaseSelect;

import java.util.List;
import java.util.Map;

/**
 * JDBC查询
 */
public class JdbcSelect {


    /**
     * 无参查询列表
     * @param sql sql语句
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static List<Map> selectList(String sql,String dataSourceName) throws Exception {
        return selectList(sql,new Object[]{},dataSourceName);
    }

    /**
     * 有参查询列表
     * @param sql sql语句
     * @param param 参数
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static List<Map> selectList(String sql, Object param, String dataSourceName) throws Exception {
        return selectList(sql,param, Map.class,dataSourceName);
    }

    /**
     * 无参查询列表，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static <T> List<T> selectList(String sql, Class<T> cls,String dataSourceName) throws Exception {
        return selectList(sql,new Object[]{},cls,dataSourceName);
    }

    /**
     * 有参查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @param dataSourceName 连接名
     * @return 数据
     */
    protected static <T> List<T> selectList(String sql, Object param, Class<T> cls,String dataSourceName) throws Exception {
       return BaseSelect.selectList(sql,param,cls,dataSourceName);
    }

    /**
     * 无参查询一条
     * @param sql sql语句
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static Map<String, Object> selectOne(String sql,String dataSourceName) throws Exception {
        return selectOne(sql,new Object[]{},dataSourceName);
    }

    /**
     * 有参查询一条
     * @param sql sql语句
     * @param param 参数
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static Map<String, Object> selectOne(String sql, Object param,String dataSourceName) throws Exception {
        List<Map> result = selectList(sql, param, dataSourceName);
        if(result != null && result.size() > 0){
            return result.get(0);
        } else if(result != null && result.size() > 1) {
            throw new Exception("查出来的数据不止一条");
        }
        return null;
    }

    /**
     * 无参查询一条，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static <T> T selectOne(String sql, Class<T> cls,String dataSourceName) throws Exception {
        return selectOne(sql,new Object[]{},cls,dataSourceName);
    }

    /**
     * 有参查询一条，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static <T> T selectOne(String sql, Object param, Class<T> cls,String dataSourceName) throws Exception {
        List<T> list = selectList(sql,param,cls,dataSourceName);
        if(list != null && list.size() > 0){
            return list.get(0);
        } else if(list != null && list.size() > 1) {
            throw new Exception("查出来的数据不止一条");
        }
        return null;
    }
}
