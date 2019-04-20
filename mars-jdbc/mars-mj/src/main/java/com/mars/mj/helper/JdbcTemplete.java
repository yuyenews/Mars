package com.mars.mj.helper;

import com.mars.core.constant.EasySpace;
import com.mars.core.util.ThreadUtil;
import com.mars.mj.manager.ConnectionManager;
import com.mars.mj.util.DataCheckUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * jdbc模板
 */
public class JdbcTemplete {

    private static EasySpace easySpace = EasySpace.getEasySpace();

    private String dataSourceName;

    private JdbcTemplete() {
    }

    /**
     * 获取JdbcTemplete对象
     *
     * @param dataSourceName
     * @return
     */
    public static JdbcTemplete get(String dataSourceName) {
        JdbcTemplete jdbcTemplete = new JdbcTemplete();
        jdbcTemplete.dataSourceName = dataSourceName;
        return jdbcTemplete;
    }

    /**
     * 查询多条数据
     *
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectList(String sql, Object param) throws Exception {
        DataCheckUtil.isNull(param,"传参不可以为null");

        ConnectionManager connectionManager = getConnection();
        try {
            List<Map<String, Object>> result = select(sql, param, connectionManager.getConnection());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
        }
    }

    /**
     * 查询多条数据
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectList(String sql) throws Exception {
        ConnectionManager connectionManager = getConnection();
        try {
            List<Map<String, Object>> result = select(sql, null, connectionManager.getConnection());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
        }
    }

    /**
     * 查询一条数据
     *
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectOne(String sql, Object param) throws Exception {
        DataCheckUtil.isNull(param,"传参不可以为null");

        ConnectionManager connectionManager = getConnection();
        try {
            List<Map<String, Object>> mapList = select(sql, param, connectionManager.getConnection());
            if (mapList != null && mapList.size() == 1) {
                return mapList.get(0);
            } else if (mapList.size() > 1) {
                throw new Exception("查出来的数据不止一条");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
        }
        return null;
    }

    /**
     * 查询一条数据
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectOne(String sql) throws Exception {
        ConnectionManager connectionManager = getConnection();
        try {
            List<Map<String, Object>> mapList = select(sql, null, connectionManager.getConnection());
            if (mapList != null && mapList.size() == 1) {
                return mapList.get(0);
            } else if (mapList.size() > 1) {
                throw new Exception("查出来的数据不止一条");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
        }
        return null;
    }

    /**
     * 增删改
     *
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public int update(String sql, Object param) throws Exception {
        DataCheckUtil.isNull(param,"传参不可以为null");

        ConnectionManager connectionManager = getConnection();
        try {
            if (param instanceof Object[]) {
                Object[] objs = (Object[]) param;
                return DBHelper.update(sql, connectionManager.getConnection(), objs);
            } else {
                return DBHelper.update(builderSql(sql, param), connectionManager.getConnection());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
        }
    }

    /**
     * 增删改
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public int update(String sql) throws Exception {
        ConnectionManager connectionManager = getConnection();
        try {
            int result = DBHelper.update(sql, connectionManager.getConnection());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws Exception
     */
    private ConnectionManager getConnection() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();

        /* 获取当前线程中的Connection */
        Object obj = easySpace.getAttr(ThreadUtil.getThreadIdToTraction());

        /* 数据库连接 */
        Connection connection = null;

        /* 获取数据源名称 */
        String dataSourceName2 = getDataSourceName(dataSourceName);

        if (obj != null) {
            Map<String, Connection> connections = (Map<String, Connection>) obj;
            connection = connections.get(dataSourceName2);
            connectionManager.setHasTrantion(false);
        } else {
            connection = DBHelper.getConnection(dataSourceName2);
            connectionManager.setHasTrantion(true);
        }
        connectionManager.setConnection(connection);
        return connectionManager;
    }

    /**
     * 查询
     *
     * @param args
     * @param connection
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> select(String sql, Object args, Connection connection) throws Exception {
        List<Map<String, Object>> result = null;
        if (args != null) {
            if (args instanceof Object[]) {
                Object[] objs = (Object[]) args;
                result = DBHelper.selectList(sql, connection, objs);
            } else {
                result = DBHelper.selectList(builderSql(sql, args), connection);
            }
        } else {
            result = DBHelper.selectList(sql, connection);
        }
        return result;
    }

    /**
     * 构建sql语句
     *
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    private String builderSql(String sql, Object args) throws Exception {

        Class cls = args.getClass();
        Object object = cls.getDeclaredConstructor().newInstance();

        /* 替换sql中的占位符 */
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            String pre = "{" + f.getName() + "}";
            if (sql.indexOf(pre) > -1) {
                sql = sql.replace(pre, f.get(object).toString());
            }
        }

        return "";
    }

    /**
     * 获取数据源名称
     *
     * @return str
     */
    private String getDataSourceName(String dataSourceName) {
        if (dataSourceName == null) {
            dataSourceName = easySpace.getAttr("defaultDataSource").toString();
        }
        return dataSourceName;
    }
}
