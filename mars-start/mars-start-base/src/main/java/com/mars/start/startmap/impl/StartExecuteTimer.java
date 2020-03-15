package com.mars.start.startmap.impl;

import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;
import com.mars.timer.execute.ExecuteMarsTimer;

public class StartExecuteTimer implements StartMap {

    /**
     * 执行定时任务
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        ExecuteMarsTimer.execute();
    }
}
