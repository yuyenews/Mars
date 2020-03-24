package com.mars.start.startmap.impl;

import com.mars.mvc.load.LoadInters;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 创建interceptor对象
 */
public class StartInter implements StartMap {

    /**
     * 创建interceptor对象
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        LoadInters.loadIntersList();
    }
}
