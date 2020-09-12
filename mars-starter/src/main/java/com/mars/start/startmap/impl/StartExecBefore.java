package com.mars.start.startmap.impl;

import com.mars.core.after.StartOnLoad;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

public class StartExecBefore implements StartMap {

    /**
     * 启动before方法
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        StartOnLoad.before();
    }
}
