package com.mars.exp;

import com.mars.exp.config.ExpConfig;
import com.mars.start.StartMars;

/**
 * 启动类
 */
public class ExpStart {

    public static void main(String[] args) {
        StartMars.start(ExpStart.class,new ExpConfig());
    }
}
