package com.mars.jdbc.core.helper.templete.base;

import com.alibaba.fastjson.JSONObject;
import com.mars.jdbc.core.helper.base.DBHelper;
import com.mars.jdbc.core.helper.manager.ConnectionManager;
import com.mars.jdbc.core.helper.templete.model.SqlBuilderModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询父类
 */
public class BaseSelect {

    /**
     * 有参查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static <T> List<T> selectList(String sql, Object param, Class<T> cls,String dataSourceName) throws Exception {
        ConnectionManager connectionManager = BaseJdbcTemplate.getConnection(dataSourceName);
        try {
            List<JSONObject> result = select(sql, param, connectionManager.getConnection());
            if (result != null && result.size() > 0) {
                List<T> resultList = new ArrayList<>();
                for (JSONObject item : result) {
                    resultList.add(item.toJavaObject(cls));
                }
                return resultList;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            connectionManager.close();
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
    protected static List<JSONObject> select(String sql, Object args, Connection connection) throws Exception {
        List<JSONObject> result = null;
        if (args != null) {
            if (args instanceof Object[]) {
                result = DBHelper.selectList(sql, connection, (Object[]) args);
            } else {
                SqlBuilderModel sqlBuilderModel = BaseJdbcTemplate.builderSql(sql, args);
                result = DBHelper.selectList(sqlBuilderModel.getSql(), connection, sqlBuilderModel.getParams());
            }
        } else {
            result = DBHelper.selectList(sql, connection);
        }
        return result;
    }
}
