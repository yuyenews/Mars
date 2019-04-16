package com.mars.junit;

/**
 * junit
 */
public class MarsJunit {

    /**
     * 加载项目启动的必要数据
     * @param packName
     */
    public void init(String packName){
        MarsJunitStart.start(null,packName,this);
    }
}
