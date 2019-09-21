package com.mars.mj.helper;

import com.mars.core.constant.MarsSpace;
import com.mars.core.util.ThreadUtil;
import com.mars.mj.manager.ConnectionManager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;

public class BaseJdbcTemplete {

    private static MarsSpace marsSpace = MarsSpace.getEasySpace();

    protected String dataSourceName;


    /**
     * 获取数据库连接
     *
     * @return
     * @throws Exception
     */
    protected ConnectionManager getConnection() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();

        /* 获取当前线程中的Connection */
        Object obj = marsSpace.getAttr(ThreadUtil.getThreadIdToTraction());

        /* 获取数据源名称 */
        String dataSourceName2 = getDataSourceName(dataSourceName);

        if (obj != null) {
            Map<String, Connection> connections = (Map<String, Connection>) obj;
            connectionManager.setConnection(connections.get(dataSourceName2));
            connectionManager.setHasTrantion(false);
        } else {
            connectionManager.setConnection(DBHelper.getConnection(dataSourceName2));
            connectionManager.setHasTrantion(true);
        }
        return connectionManager;
    }



    /**
     * 获取数据源名称
     *
     * @return str
     */
    protected String getDataSourceName(String dataSourceName) {
        if (dataSourceName == null) {
            dataSourceName = marsSpace.getAttr("defaultDataSource").toString();
        }
        return dataSourceName;
    }

    /**
     * 构建sql语句
     *
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected static String builderSql(String sql, Object args) throws Exception {

        Class cls = args.getClass();

        /* 替换sql中的占位符 */
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String pre = "{" + f.getName() + "}";
            if (sql.indexOf(pre) > -1) {
                String ftname = f.getType().getName();
                if(ftname.equals(String.class.getName()) || ftname.equals(Character.class.getName())){
                    sql = sql.replace(pre, "'"+f.get(args).toString()+"'");
                } else {
                    sql = sql.replace(pre, f.get(args).toString());
                }
            }
        }

        return sql;
    }
}
