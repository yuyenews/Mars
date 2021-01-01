package com.mars.start.startmap.impl;

import com.mars.common.ncfg.mvc.DispatcherFactory;
import com.mars.mvc.servlet.MarsCoreDispatcher;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 配置核心servlet
 */
public class StartCoreDispatcher implements StartMap {

    /**
     * 配置核心servlet
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        DispatcherFactory.setDispatcher(new MarsCoreDispatcher());
    }
}
