package com.yuyenews.mj.model;

import java.sql.Connection;

/**
 * 连接model
 */
public class ConnectionModel {

    /**
     * 是否有事务管理
     */
    private boolean hasTrantion;

    /**
     * 数据库连接
     */
    private Connection connection;

    public void close() throws Exception {
        if(hasTrantion){
            connection.close();
        }
    }

    public void setHasTrantion(boolean hasTrantion) {
        this.hasTrantion = hasTrantion;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
