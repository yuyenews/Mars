package com.mars.start.startmap.impl;

import com.mars.core.load.LoadClass;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 扫描包下面的类
 */
public class StartLoadClass implements StartMap {

    /**
     * 扫描包
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        /*获取要扫描的包*/
        String className = getClassName(startParam);

        /* 获取此包下面的所有类（包括jar中的） */
        LoadClass.scanClass(className);
    }

    /**
     * 截取main方法所在包名
     * @param startParam 类
     * @return 返回值
     * @throws Exception 异常
     */
    private String getClassName(StartParam startParam) throws Exception {
        String className = startParam.getClazz().getName();
        if(className.lastIndexOf(".") > 0){
            className = className.substring(0,className.lastIndexOf("."));
        }

        if(className.indexOf(".") < 0){
            throw new Exception("启动服务的main方法所在的类,必须放在两层包名中,比如[com.mars,com.test]等,不允许放在[com,cn]等包中,更不允许放在包外面");
        }
        return className;
    }
}
