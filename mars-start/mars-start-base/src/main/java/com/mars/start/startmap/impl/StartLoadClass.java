package com.mars.start.startmap.impl;

import com.mars.core.annotation.MarsScan;
import com.mars.core.load.LoadClass;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

/**
 * 扫描包下面的类
 */
public class StartLoadClass implements StartMap {

    /**
     * 扫描包
     *
     * @param startParam 参数
     * @throws Exception 异常
     */
    @Override
    public void load(StartParam startParam) throws Exception {
        /*获取要扫描的包*/
        String[] className = getClassName(startParam);

        /* 获取此包下面的所有类（包括jar中的） */
        LoadClass.scanClass(className);
    }

    /**
     * 截取main方法所在包名
     *
     * @param startParam 类
     * @return 返回值
     * @throws Exception 异常
     */
    private String[] getClassName(StartParam startParam) throws Exception {

        Class<?> cls = startParam.getClazz();
        String[] classNameList = null;

        MarsScan marsScan = cls.getAnnotation(MarsScan.class);
        if (marsScan != null) {
            classNameList = marsScan.packageName();
        } else {
            String cName = cls.getName();
            if (cName.lastIndexOf(".") > 0) {
                classNameList = new String[1];
                classNameList[0] = cName.substring(0, cName.lastIndexOf("."));
            }
        }

        if(classNameList == null){
            throw new Exception("请正确配置要扫描的包名");
        }

        for (String className : classNameList) {
            if (className.indexOf(".") < 0) {
                throw new Exception("Mars只可以扫描多层包名,比如[com.mars,com.test]等,无法扫描此包[" + className + "]");
            }
        }
        return classNameList;
    }
}
