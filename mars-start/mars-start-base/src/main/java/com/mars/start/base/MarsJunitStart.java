package com.mars.start.base;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadHelper;
import com.mars.core.load.WriteFields;
import com.mars.jdbc.load.InitJdbc;
import com.mars.start.startmap.StartMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 单测启动器
 */
public class MarsJunitStart {

    private static Logger log = LoggerFactory.getLogger(MarsJunitStart.class);

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    private static Map<Integer, StartMap> startList;

    public static void setStartList(Map<Integer, StartMap> startList) {
        MarsJunitStart.startList = startList;
    }

    /**
     * 启动Mars框架
     */
    public static void start(InitJdbc initJdbc, Class<?> packName, Object obj) {
        try {
            if(constants.getAttr(MarsConstant.HAS_TEST) == null){
                log.info("程序启动中......");

                /* 加载框架数据 */
                StartLoad.load(initJdbc,packName,startList);

                /* 标记已经为单测创建过资源了 */
                constants.setAttr(MarsConstant.HAS_TEST,"yes");
            }

            /* 给单测注入属性 */
            autoWrite(obj);

            /* 标识是否已经启动 */
            constants.setAttr(MarsConstant.HAS_NETTY_START,"yes");

            log.info("开始执行单测......");
        } catch (Exception e) {
            log.error("启动失败",e);
        }
    }

    /**
     * 给单测注入属性
     */
    public static void autoWrite(Object obj) {
        try {
            Class cls = obj.getClass();
            WriteFields.writeFields(cls,obj,LoadHelper.getBeanObjectMap());
        } catch (Exception e){
            log.error("初始化单测出现异常",e);
        }
    }
}
