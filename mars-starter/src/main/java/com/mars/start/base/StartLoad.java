package com.mars.start.base;

import com.mars.jdbc.load.InitJdbc;
import com.mars.start.startmap.StartMap;
import com.mars.start.startmap.StartParam;

import java.util.Map;

/**
 * 加载所需的资源
 */
public class StartLoad {

    /**
     * 加载所需的资源
     * @param initJdbc jdbc加载器
     * @param packName 要扫描的包
     * @param startList 要加载的责任链
     * @throws Exception 异常
     */
    public static void load(InitJdbc initJdbc, Class<?> packName, Map<Integer, StartMap> startList) throws Exception{

        StartParam startParam = new StartParam();
        startParam.setClazz(packName);
        startParam.setInitJdbc(initJdbc);

        for(int i=0; i < startList.size(); i++){
            startList.get(i).load(startParam);
        }
    }
}
