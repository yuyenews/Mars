package com.yuyenews.mj.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.util.ConfigUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC连接帮助类
 */
public class DBHelper {

    /**
     * 数据源对象集合
     */
    private static Map<String, DruidDataSource> druidDataSources;
    /**
     * sql语句预编译处理接口
     */
    private static PreparedStatement preparedStatement;
    /**
     * sql语句处理接口
     */
    private static Statement statement;

    /**
     * 无条件查询
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @return 结果集
     * @throws Exception
     */
    public static List<Map<String, Object>> selectList(String sql, Connection connection) throws Exception {
        return selectList(sql, connection, new Object[]{});
    }

    /**
     * 有条件查询
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @param params     参数
     * @return 结果集
     * @throws Exception
     */
    public static List<Map<String, Object>> selectList(String sql, Connection connection, Object[] params) throws Exception {
        ResultSet resultSet = select(sql, connection, params);
        List<Map<String, Object>> list = new ArrayList<>();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int count = resultSetMetaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> rows = new HashMap<>();
            for (int i = 1; i <= count; i++) {
                String key = resultSetMetaData.getColumnLabel(i);
                String value = resultSet.getString(key);
                rows.put(key, value);
            }
            list.add(rows);
        }
        return null;
    }

    /**
     * 无条件查询
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @return 结果集
     * @throws Exception
     */
    public static ResultSet select(String sql, Connection connection) throws Exception {
        return select(sql, connection, new Object[]{});
    }

    /**
     * 有条件查询
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @param params     参数
     * @return 结果集
     * @throws Exception
     */
    public static ResultSet select(String sql, Connection connection, Object[] params) throws Exception {
        if (params != null && params.length > 0) {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeQuery();
        } else {
            statement = connection.createStatement();
            return statement.executeQuery(sql);
        }

    }

    /**
     * 无条件增删改
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @return 受影响行数
     * @throws Exception
     */
    public static int update(String sql, Connection connection) throws Exception {
        return update(sql, connection, new Object[]{});
    }

    /**
     * 有条件增删改
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @param params     参数
     * @return 受影响行数
     * @throws Exception
     */
    public static int update(String sql, Connection connection, Object[] params) throws Exception {
        if(params != null && params.length > 0){
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate();
        } else {
            statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }
    }

    /**
     * 获取所有的数据源对象
     * @return
     * @throws Exception
     */
    public static Map<String,DruidDataSource> getDruidDataSources() throws Exception {
        init();
        return druidDataSources;
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String dataSourceName) throws Exception {
        init();
        Connection connection = druidDataSources.get(dataSourceName).getConnection();
        connection.setAutoCommit(true);
        return connection;
    }

    /**
     * 初始化
     *
     * @return
     * @throws Exception
     */
    private static void init() throws Exception {
        if (druidDataSources == null) {
            initConnections();
        }
    }

    /**
     * 加载JDBC连接
     *
     * @throws Exception
     */
    private static void initConnections() throws Exception {
        if(druidDataSources == null){
            druidDataSources = new HashMap<>();

            JSONObject jdbcConfig = ConfigUtil.getJdbcConfig();

            JSONArray dataSourceList = jdbcConfig.getJSONArray(EasyConstant.DATA_SOURCE);
            if (dataSourceList != null) {
                for (int i = 0; i < dataSourceList.size(); i++) {
                    JSONObject dataSource = dataSourceList.getJSONObject(i);
                    DruidDataSource druidDataSource = initDataSource(dataSource);
                    druidDataSources.put(dataSource.getString("name"), druidDataSource);
                }
            }
        }

    }

    /**
     * 获取 DruidDataSource对象
     *
     * @param dataSource 数据源配置
     * @return DruidDataSource对象
     * @throws Exception
     */
    private static DruidDataSource initDataSource(JSONObject dataSource) throws Exception {

        Class cls = Class.forName(EasyConstant.DRUID_DATA_SOURCE);
        Object druidDataSource = cls.getDeclaredConstructor().newInstance();

        for (String key : dataSource.keySet()) {

            /* 获取对象属性，完成注入 */
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals(key)) {
                    f.setAccessible(true);
                    f.set(druidDataSource, dataSource.get(key));
                }
            }

        }
        return (DruidDataSource) druidDataSource;
    }
}
