package com.mars.start;

import com.mars.mj.init.InitJdbc;
import com.mars.start.base.BaseStartMars;

/**
 * 启动Mars框架
 * @author yuye
 *
 */
public class StartMars {
    /**
     * 启动Mars框架
     * @param clazz
     */
    public static void start(Class<?> clazz,String[] args){
        if(args != null && args[0] != null){
            BaseStartMars.start(clazz,new InitJdbc(),args[0]);
        } else {
            start(clazz);
        }
    }

    /**
     * 启动Mars框架
     * @param clazz
     */
    public static void start(Class<?> clazz){
        BaseStartMars.start(clazz,new InitJdbc(),null);
    }
}
