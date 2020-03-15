package com.mars.start.startmap.impl;

import com.mars.ioc.load.LoadMarsBean;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 创建bean对象
 */
public class StartBeanObject implements StartMap {

    /**
     * 创建bean对象
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        LoadMarsBean.loadBean();
    }
}