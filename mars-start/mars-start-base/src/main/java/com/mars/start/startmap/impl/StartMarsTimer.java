package com.mars.start.startmap.impl;

import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;
import com.mars.timer.load.LoadMarsTimer;

/**
 * 加载timer对象
 */
public class StartMarsTimer implements StartMap {

    /**
     * 加载timer对象
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        LoadMarsTimer.loadMarsTimers();
    }
}
