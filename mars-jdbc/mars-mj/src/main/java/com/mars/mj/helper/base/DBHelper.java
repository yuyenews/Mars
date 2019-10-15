package com.mars.mj.helper.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.jdbc.util.JdbcConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * JDBC连接帮助类
 */
public class DBHelper {

    private static Logger logger = LoggerFactory.getLogger(DBHelper.class);

    /**
     * 数据源对象集合
     */
    private static Map<String, DruidDataSource> druidDataSources;
    /**
     * sql语句预编译处理接口
     */
    private static PreparedStatement preparedStatement;
    /**
     * 默认数据源名称
     */
    private static String defaultDataSourceName;

    public static String getDefaultDataSourceName(){
        return defaultDataSourceName;
    }

    /**
     * 无条件查询
     *
     * @param sql        sql语句
     * @param connection 数据库连接
     * @return 结果集
     * @throws Exception
     */
    public static List<JSONObject> selectList(String sql, Connection connection) throws Exception {
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
    public static List<JSONObject> selectList(String sql, Connection connection, Object[] params) throws Exception {
        ResultSet resultSet = select(sql, connection, params);
        List<JSONObject> list = new ArrayList<>();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int count = resultSetMetaData.getColumnCount();

        while (resultSet.next()) {
            JSONObject rows = new JSONObject();
            for (int i = 1; i <= count; i++) {
                String key = resultSetMetaData.getColumnLabel(i);
                Object value = resultSet.getObject(i);
                rows.put(key, value);
            }
            list.add(rows);
        }
        return list;
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
        logger.info("sql:{}",sql);

        preparedStatement = connection.prepareStatement(sql);
        if(params != null && params.length > 0){
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        return preparedStatement.executeQuery();
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
        return update(sql, connection, null);
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
        logger.info("sql:{}",sql);

        preparedStatement = connection.prepareStatement(sql);
        if(params != null && params.length > 0){
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        return preparedStatement.executeUpdate();
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
    private static synchronized void init() throws Exception {
        if (druidDataSources != null) {
            return;
        }
        Object dataSourceMap = MarsSpace.getEasySpace().getAttr(MarsConstant.DATA_SOURCE_MAP);
        if(dataSourceMap != null){
            druidDataSources = (Map<String, DruidDataSource>)dataSourceMap;
            return;
        }
        initConnections();
    }

    /**
     * 加载JDBC连接
     *
     * @throws Exception
     */
    private static void initConnections() throws Exception {
        druidDataSources = new HashMap<>();

        JSONArray dataSourceList = JdbcConfigUtil.getJdbcDataSourceList();
        if (dataSourceList != null) {
            for (int i = 0; i < dataSourceList.size(); i++) {
                JSONObject dataSource = dataSourceList.getJSONObject(i);
                DruidDataSource druidDataSource = initDataSource(dataSource);
                druidDataSources.put(dataSource.getString("name"), druidDataSource);
                if (i == 0) {
                    defaultDataSourceName = dataSource.getString("name");
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

        DruidDataSource druidDataSource = new DruidDataSource();

        Properties properties = new Properties();

        /* 设置默认属性 */
        properties.setProperty("druid.maxWait","60000");
        properties.setProperty("druid.timeBetweenEvictionRunsMillis","2000");
        properties.setProperty("druid.minEvictableIdleTimeMillis","5000");

        for (String key : dataSource.keySet()) {
            /* 如果配置里有上面的属性，则以配置中的为准 */
            properties.setProperty("druid."+key,dataSource.get(key).toString());
        }

        druidDataSource.configFromPropety(properties);

        return druidDataSource;
    }

}
