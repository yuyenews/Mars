package com.mars.start.startmap.impl;

import com.mars.common.annotation.start.MarsImport;
import com.mars.core.load.LoadClass;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

import java.util.ArrayList;
import java.util.List;

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
        List<String> className = getClassName(startParam);

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
    private List<String> getClassName(StartParam startParam) throws Exception {
        List<String> classNameList = new ArrayList<>();

        Class<?> cls = startParam.getClazz();
        String cName = cls.getName();
        if (cName.lastIndexOf(".") > 0) {
            cName = cName.substring(0, cName.lastIndexOf("."));
        }
        classNameList.add(cName);

        classNameList = getImportList(cls,classNameList);

        for (String className : classNameList) {
            if (className.indexOf(".") < 0) {
                throw new Exception("启动类所在的包名以及MarsImport导入的包名必须是两层或两层以上，比如[com.demo,com.test]等");
            }
        }

        return classNameList;
    }

    /**
     * 获取导入的包名
     *
     * @param cls 启动类
     * @param classNameList 包名集合
     * @return 包名集合
     */
    private List<String> getImportList(Class<?> cls, List<String> classNameList) {
        MarsImport marsImport = cls.getAnnotation(MarsImport.class);
        if (marsImport != null) {
            for(String packageItem : marsImport.packageName()) {
                classNameList.add(packageItem);
            }
        }
        return classNameList;
    }
}
