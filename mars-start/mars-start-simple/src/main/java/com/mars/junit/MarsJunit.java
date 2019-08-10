package com.mars.junit;

/**
 * junit
 */
public abstract class MarsJunit {

    /**
     * 加载项目启动的必要数据
     * @param packName
     */
    public void init(String packName,String suffix){
        MarsJunitStart.start(null,packName,this,null,suffix);
    }

    /**
     * 加载项目启动的必要数据
     * @param packName
     */
    public void init(String packName){
        init(packName,null);
    }

    /**
     * 单测开始前
     */
    public abstract void before();
}
