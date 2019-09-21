package com.mars.mj.helper;

import com.mars.mj.manager.ConnectionManager;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * JDBC查询
 */
public class JdbcSelect {


    /**
     * 无参查询列表
     * @param sql sql语句
     * @param connection 连接
     * @return 数据
     */
    public static List<Map<String, Object>> selectList(String sql, ConnectionManager connection) throws Exception {
        return selectList(sql,null,connection);
    }

    /**
     * 有参查询列表
     * @param sql sql语句
     * @param param 参数
     * @param connection 连接
     * @return 数据
     */
    public static List<Map<String, Object>> selectList(String sql, Object param, ConnectionManager connection) throws Exception {
        try {
            List<Map<String, Object>> result = select(sql, param, connection.getConnection());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            connection.close();
        }
    }

    /**
     * 无参查询列表，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @param connection 连接
     * @return 数据
     */
    public static <T> List<T> selectList(String sql, Class<T> cls, ConnectionManager connection) throws Exception {
        return selectList(sql,null,cls,connection);
    }

    /**
     * 有参查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @param connection 连接
     * @return 数据
     */
    public static <T> List<T> selectList(String sql, Object param, Class<T> cls, ConnectionManager connection) throws Exception {
        List<Map<String, Object>> mapList = selectList(sql,param,connection);
        return null;
    }

    /**
     * 无参查询一条
     * @param sql sql语句
     * @param connection 连接
     * @return 数据
     */
    public static Map<String, Object> selectOne(String sql, ConnectionManager connection) throws Exception {
        return selectOne(sql,null,connection);
    }

    /**
     * 有参查询一条
     * @param sql sql语句
     * @param param 参数
     * @param connection 连接
     * @return 数据
     */
    public static Map<String, Object> selectOne(String sql, Object param, ConnectionManager connection) throws Exception {
        List<Map<String, Object>> result = selectList(sql, param, connection);
        if(result != null && result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    /**
     * 无参查询一条，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @param connection 连接
     * @return 数据
     */
    public static <T> T selectOne(String sql, Class<T> cls, ConnectionManager connection) throws Exception {
        return selectOne(sql,null,cls,connection);
    }

    /**
     * 有参查询一条，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @param connection 连接
     * @return 数据
     */
    public static <T> T selectOne(String sql, Object param, Class<T> cls, ConnectionManager connection) throws Exception {
        List<T> list = selectList(sql,param,cls,connection);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询
     *
     * @param args
     * @param connection
     * @return
     * @throws Exception
     */
    private static List<Map<String, Object>> select(String sql, Object args, Connection connection) throws Exception {
        List<Map<String, Object>> result = null;
        if (args != null) {
            if (args instanceof Object[]) {
                Object[] params = (Object[]) args;
                result = DBHelper.selectList(sql, connection, params);
            } else {
                result = DBHelper.selectList(BaseJdbcTemplete.builderSql(sql, args), connection, null);
            }
        } else {
            result = DBHelper.selectList(sql, connection);
        }
        return result;
    }
}
