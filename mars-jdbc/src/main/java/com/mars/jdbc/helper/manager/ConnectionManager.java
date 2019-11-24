package com.mars.jdbc.helper.manager;

import java.sql.Connection;

/**
 * 连接model
 */
public class ConnectionManager {

    /**
     * 是否有事务管理
     */
    private boolean hasTraction;

    /**
     * 数据库连接
     */
    private Connection connection;

    public void close() throws Exception {
        if(hasTraction){
            /*
             * 如果为true，代表没有配置事务管理，所以会自动提交，也需要立刻关闭，
             * 否则将由事务管理器去关闭连接
             */
            connection.close();
        }
    }

    public void setHasTraction(boolean hasTraction) {
        this.hasTraction = hasTraction;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
