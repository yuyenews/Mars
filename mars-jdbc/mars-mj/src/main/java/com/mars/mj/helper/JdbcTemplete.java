package com.mars.mj.helper;

import com.mars.core.constant.MarsSpace;
import com.mars.mj.manager.ConnectionManager;
import com.mars.mj.util.DataCheckUtil;

import java.util.List;
import java.util.Map;

/**
 * jdbc模板
 */
public class JdbcTemplete extends BaseJdbcTemplete {

    private static MarsSpace marsSpace = MarsSpace.getEasySpace();

    private JdbcTemplete() {

    }

    /**
     * 获取JdbcTemplete对象
     *
     * @return
     */
    public static JdbcTemplete get() {
        JdbcTemplete jdbcTemplete = new JdbcTemplete();
        jdbcTemplete.dataSourceName = jdbcTemplete.getDataSourceName(null);
        return jdbcTemplete;
    }

    /**
     * 获取JdbcTemplete对象
     *
     * @param dataSourceName
     * @return
     */
    public static JdbcTemplete get(String dataSourceName) {
        JdbcTemplete jdbcTemplete = new JdbcTemplete();
        jdbcTemplete.dataSourceName = jdbcTemplete.getDataSourceName(dataSourceName);
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
        ConnectionManager connectionManager = getConnection();
        return JdbcSelect.selectList(sql,param,connectionManager);
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
        return JdbcSelect.selectList(sql,connectionManager);
    }

    /**
     * 无参查询列表，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @return 数据
     */
    public <T> List<T> selectList(String sql, Class<T> cls) throws Exception {
        ConnectionManager connectionManager = getConnection();
        return JdbcSelect.selectList(sql,cls,connectionManager);
    }

    /**
     * 有参查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @return 数据
     */
    public <T> List<T> selectList(String sql, Object param, Class<T> cls) throws Exception {
        ConnectionManager connectionManager = getConnection();
        return JdbcSelect.selectList(sql,param,cls,connectionManager);
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
        ConnectionManager connectionManager = getConnection();
        return JdbcSelect.selectOne(sql,param,connectionManager);
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
        return JdbcSelect.selectOne(sql,connectionManager);
    }


    /**
     * 无参查询一条，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @return 数据
     */
    public <T> T selectOne(String sql, Class<T> cls) throws Exception {
        ConnectionManager connectionManager = getConnection();
        return JdbcSelect.selectOne(sql,cls,connectionManager);
    }

    /**
     * 有参查询一条，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @return 数据
     */
    public <T> T selectOne(String sql, Object param, Class<T> cls) throws Exception {
        ConnectionManager connectionManager = getConnection();
        return JdbcSelect.selectOne(sql,param,cls,connectionManager);
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
                Object[] params = (Object[]) param;
                return DBHelper.update(sql, connectionManager.getConnection(), params);
            } else {
                return DBHelper.update(BaseJdbcTemplete.builderSql(sql, param), connectionManager.getConnection(), null);
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


}
