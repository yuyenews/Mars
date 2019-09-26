package com.mars.mj.helper.templete;

import com.mars.mj.helper.base.DBHelper;
import com.mars.mj.manager.ConnectionManager;
import com.mars.mj.model.SqlBuilderModel;

public class JdbcUpdate {

    /**
     * 增删改
     *
     * @param sql sql语句
     * @param param 参数
     * @param dataSourceName 连接名
     * @return
     * @throws Exception
     */
    public static int update(String sql, Object param, String dataSourceName) throws Exception {
        ConnectionManager connectionManager = BaseJdbcTemplate.getConnection(dataSourceName);
        try {
            if (param instanceof Object[]) {
                Object[] params = (Object[]) param;
                return DBHelper.update(sql, connectionManager.getConnection(), params);
            } else {
                SqlBuilderModel sqlBuilderModel = BaseJdbcTemplate.builderSql(sql, param);
                return DBHelper.update(sqlBuilderModel.getSql(), connectionManager.getConnection(), sqlBuilderModel.getParams());
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
     * @param sql sql语句
     * @param dataSourceName 连接名
     * @return
     * @throws Exception
     */
    public static int update(String sql, String dataSourceName) throws Exception {
        ConnectionManager connectionManager = BaseJdbcTemplate.getConnection(dataSourceName);
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
