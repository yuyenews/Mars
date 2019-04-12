package com.mars.start;

import com.mars.mj.init.InitJdbc;
import com.mars.start.base.BaseStartEasy;

public class StartEasy {
    /**
     * 启动easy框架
     * @param clazz
     */
    public static void start(Class<?> clazz){
        BaseStartEasy.start(clazz,new InitJdbc());
    }
}
