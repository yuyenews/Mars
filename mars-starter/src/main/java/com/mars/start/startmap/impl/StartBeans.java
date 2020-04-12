package com.mars.start.startmap.impl;

import com.mars.core.load.LoadBeans;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 扫描包
 */
public class StartBeans implements StartMap {

    @Override
    public void load(StartParam startParam) throws Exception {
        LoadBeans.loadBeans();
    }
}
