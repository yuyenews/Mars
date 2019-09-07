package com.mars.core.load;

import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.Resource;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.model.MarsTimerModel;
import com.mars.core.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载工具类
 */
public class LoadHelper {

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 获取Bean的名称
     * @param map 集合
     * @param cls 类
     * @return 名称
     */
    public static String getBeanName(Map<String,Object> map,Class<?> cls){
        MarsBean marsBean = (MarsBean)map.get("annotation");
        String beanName = marsBean.value();
        return getBeanName(beanName,cls);
    }

    /**
     * 获取Bean的名称
     * @param beanName bean名称
     * @param cls 类
     * @return 名称
     */
    public static String getBeanName(String beanName,Class<?> cls){
        if(beanName == null || beanName.equals("")) {
            beanName = StringUtil.getFirstLowerCase(cls.getSimpleName());
        }
        return beanName;
    }

    /**
     * 获取字段名
     * @param resource 资源
     * @param f 字段
     * @return 名称
     */
    public static String getResourceName(Resource resource, Field f){
        String filedName = resource.value();
        if(filedName == null || filedName.equals("")) {
            filedName = f.getName();
        }
        return filedName;
    }

    /**
     * 获取所有的加了MarsBean注解的基础数据
     * @return 数据
     */
    public static List<Map<String,Object>> getBeanList(){
        Object marsBeans = constants.getAttr(MarsConstant.MARS_BEANS);
        List<Map<String,Object>> marsBeansList = new ArrayList<>();
        if(marsBeans != null) {
            marsBeansList = (List<Map<String,Object>>)marsBeans;
        }
        return marsBeansList;
    }

    /**
     * 获取所有的bean对象
     * @return 集合
     */
    public static Map<String, MarsBeanModel> getBeanObjectMap(){
        Object objs2 = constants.getAttr(MarsConstant.MARS_BEAN_OBJECTS);
        Map<String, MarsBeanModel> marsBeanObjects = new HashMap<>();
        if(objs2 != null) {
            marsBeanObjects = (Map<String, MarsBeanModel>)objs2;
        }
        return marsBeanObjects;
    }

    /**
     * 获取所有的加了MarsDao注解的基础数据
     * @return 数据
     */
    public static List<Map<String, Object>>  getDaoList(){
        Object objs = constants.getAttr(MarsConstant.MARS_DAOS);
        List<Map<String, Object>> easyDaos = new ArrayList<>();
        if(objs != null) {
            easyDaos = (List<Map<String, Object>>) objs;
        }
        return easyDaos;
    }

    /**
     * 获取所有配了定时任务的基础数据
     * @return 数据
     */
    public static List<MarsTimerModel> getMarsTimersList(){
        Object objs = constants.getAttr(MarsConstant.MARS_TIMER_OBJECTS);
        List<MarsTimerModel> marsTimerObjects = new ArrayList<>();
        if(objs != null) {
            marsTimerObjects = (List<MarsTimerModel>)objs;
        }
        return marsTimerObjects;
    }

    /**
     * 获取所有的加了Controller注解的基础数据
     * @return 数据
     */
    public static List<Map<String,Object>> getControllerList(){
        Object objs = constants.getAttr(MarsConstant.CONTROLLERS);
        List<Map<String,Object>> contorls = new ArrayList<>();
        if(objs != null) {
            contorls = (List<Map<String,Object>>)objs;
        }
        return contorls;
    }
}
