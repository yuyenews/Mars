package com.mars.start;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.util.MarsConfiguration;
import com.mars.jdbc.core.load.InitJdbc;
import com.mars.start.base.BaseStartMars;
import com.mars.start.startmap.StartLoadList;

/**
 * 启动Mars框架
 * @author yuye
 *
 */
public class StartMars {
    /**
     * 启动Mars框架
     * @param clazz
     */
    public static void start(Class<?> clazz, MarsConfig marsConfig){
        BaseStartMars.setStartList(StartLoadList.initStartList());
        if(marsConfig != null){
            MarsConfiguration.loadConfig(marsConfig);
        }
        BaseStartMars.start(clazz,new InitJdbc());
    }
}
