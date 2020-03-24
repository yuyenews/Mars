package com.mars.start.base;

import com.mars.core.annotation.MarsTest;
import com.mars.core.base.config.MarsConfig;
import com.mars.core.util.MarsConfiguration;
import com.mars.jdbc.load.InitJdbc;
import com.mars.start.startmap.StartLoadList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单测基类
 */
public abstract class BaseJunit {

    private Logger logger = LoggerFactory.getLogger(BaseJunit.class);

    public BaseJunit(){
        MarsTest marsTest = this.getClass().getAnnotation(MarsTest.class);
        if(marsTest == null || marsTest.startClass() == null){
            logger.error("没有正确的配置MarsTest注解");
        } else {
            init(marsTest.startClass());
        }
    }

    /**
     * 获取配置信息
     * @return
     */
    public abstract MarsConfig getMarsConfig();

    /**
     * 加载单测需要的资源
     * @param packName 包
     */
    public void init(Class packName){
        MarsConfiguration.loadConfig(getMarsConfig());
        MarsJunitStart.setStartList(StartLoadList.initTestStartList());
        MarsJunitStart.start(getInitJdbc(),packName,this);
    }

    /**
     * 获取加载jdbc的类
     * @return 类
     */
    public abstract InitJdbc getInitJdbc();
}
