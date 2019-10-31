package com.mars.core.load;

import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.MarsWrite;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanClassModel;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.model.MarsTimerModel;
import com.mars.core.util.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

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
     * @param marsBeanClassModel 集合
     * @param cls 类
     * @return 名称
     */
    public static String getBeanName(MarsBeanClassModel marsBeanClassModel,Class<?> cls){
        MarsBean marsBean = (MarsBean)marsBeanClassModel.getAnnotation();
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
     * @param marsWrite 资源
     * @param f 字段
     * @return 名称
     */
    public static String getResourceName(MarsWrite marsWrite, Field f){
        String filedName = null;
        if(marsWrite != null){
            filedName = marsWrite.value();
        }
        if(filedName == null || filedName.equals("")) {
            filedName = f.getName();
        }
        return filedName;
    }

    /**
     * 获取所有的加了MarsBean注解的基础数据
     * @return 数据
     */
    public static List<MarsBeanClassModel> getBeanList(){
        List<MarsBeanClassModel> marsBeansList = new ArrayList<>();

        Object marsBeans = constants.getAttr(MarsConstant.MARS_BEANS);
        if(marsBeans != null) {
            marsBeansList = (List<MarsBeanClassModel>)marsBeans;
        }
        return marsBeansList;
    }

    /**
     * 获取所有的bean对象
     * @return 集合
     */
    public static Map<String, MarsBeanModel> getBeanObjectMap(){
        Map<String, MarsBeanModel> marsBeanObjects = new HashMap<>();

        Object object = constants.getAttr(MarsConstant.MARS_BEAN_OBJECTS);
        if(object != null) {
            marsBeanObjects = (Map<String, MarsBeanModel>)object;
        }
        return marsBeanObjects;
    }

    /**
     * 获取所有的加了MarsDao注解的基础数据
     * @return 数据
     */
    public static List<MarsBeanClassModel>  getDaoList(){
        List<MarsBeanClassModel> marsDaos = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.MARS_DAOS);
        if(object != null) {
            marsDaos = (List<MarsBeanClassModel>)object;
        }
        return marsDaos;
    }

    /**
     * 获取所有配了定时任务的基础数据
     * @return 数据
     */
    public static List<MarsTimerModel> getMarsTimersList(){
        List<MarsTimerModel> marsTimerObjects = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.MARS_TIMER_OBJECTS);
        if(object != null) {
            marsTimerObjects = (List<MarsTimerModel>)object;
        }
        return marsTimerObjects;
    }

    /**
     * 获取所有的加了MarsApi注解的基础数据
     * @return 数据
     */
    public static List<MarsBeanClassModel> getMarsApiList(){
        List<MarsBeanClassModel> contorls = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.CONTROLLERS);
        if(object != null) {
            contorls = (List<MarsBeanClassModel>)object;
        }
        return contorls;
    }

    /**
     * 获取所有的加了MarsInterceptor注解的基础数据
     * @return
     */
    public static List<MarsBeanClassModel> getInterceptorList(){
        List<MarsBeanClassModel> interceptors = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.INTERCEPTORS);
        if(object != null) {
            interceptors = (List<MarsBeanClassModel>)object;
        }
        return interceptors;
    }

    /**
     * 获取所有的加了MarsAfter注解的基础数据
     * @return
     */
    public static List<Class> getMarsAfterList(){
        List<Class> marsAfters = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.MARS_AFTERS);
        if(object != null) {
            marsAfters = (List<Class>)object;
        }
        return marsAfters;
    }

    /**
     * 获取扫描出来的类
     * @return
     */
    public static Set<String> getSacnClassList(){
        Set<String> classList = new HashSet<>();

        Object object = constants.getAttr(MarsConstant.SCAN_ALL_CLASS);
        if (object != null) {
            classList = (Set<String>) object;
        }
        return classList;
    }
}
