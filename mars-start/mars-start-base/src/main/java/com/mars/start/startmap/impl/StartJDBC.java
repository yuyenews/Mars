package com.mars.start.startmap.impl;

import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 加载JDBC模块
 */
public class StartJDBC implements StartMap {

    /**
     * 加载JDBC模块
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        if(startParam.getInitJdbc() != null){
            startParam.getInitJdbc().init();
        }
    }
}
