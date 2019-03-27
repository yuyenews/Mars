package com.yuyenews.start;

import com.yuyenews.easy.mj.init.InitJdbc;
import com.yuyenews.start.base.BaseStartEasy;

public class StartEasy {
    /**
     * 启动easy框架
     * @param clazz
     */
    public static void start(Class<?> clazz){
        BaseStartEasy.start(clazz,new InitJdbc());
    }
}
