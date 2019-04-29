package com.mars.junit;

/**
 * junit
 */
public abstract class MarsJunit {

    /**
     * 加载项目启动的必要数据
     * @param packName
     */
    public void init(String packName){
        MarsJunitStart.start(null,packName,this,null);
    }

    /**
     * 单测开始前
     */
    public abstract void before();
}
