package com.mars.core.load;

import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.bean.MarsWrite;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.StringUtil;
import com.mars.core.model.MarsBeanClassModel;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.model.MarsTimerModel;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 加载工具类
 */
public class LoadHelper {

    /**
     * 获取全局存储空间
     */
    private static final MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 获取Bean的名称
     *
     * @param marsBeanClassModel 集合
     * @param cls                类
     * @return 名称
     */
    public static String getBeanName(MarsBeanClassModel marsBeanClassModel, Class<?> cls) {
        MarsBean marsBean = (MarsBean) marsBeanClassModel.getAnnotation();
        String beanName = marsBean.value();
        return getBeanName(beanName, cls);
    }

    /**
     * 获取Bean的名称
     *
     * @param beanName bean名称
     * @param cls      类
     * @return 名称
     */
    public static String getBeanName(String beanName, Class<?> cls) {
        if (beanName == null || beanName.equals("")) {
            beanName = StringUtil.getFirstLowerCase(cls.getSimpleName());
        }
        return beanName;
    }

    /**
     * 获取字段名
     *
     * @param marsWrite 资源
     * @param f         字段
     * @return 名称
     */
    public static String getResourceName(MarsWrite marsWrite, Field f) {
        String filedName = null;
        if (marsWrite != null) {
            filedName = marsWrite.value();
        }
        if (filedName == null || filedName.equals("")) {
            filedName = f.getName();
        }
        return filedName;
    }

    /**
     * 获取所有的加了MarsBean注解的基础数据
     *
     * @return 数据
     */
    public static List<MarsBeanClassModel> getBeanList() {
        List<MarsBeanClassModel> marsBeansList = new ArrayList<>();

        Object marsBeans = constants.getAttr(MarsConstant.MARS_BEANS);
        return marsBeans != null ? (List<MarsBeanClassModel>) marsBeans : marsBeansList;
    }

    /**
     * 获取所有的bean对象
     *
     * @return 集合
     */
    public static Map<String, MarsBeanModel> getBeanObjectMap() {
        Map<String, MarsBeanModel> marsBeanObjects = new HashMap<>();

        Object object = constants.getAttr(MarsConstant.MARS_BEAN_OBJECTS);
        return object != null ? (Map<String, MarsBeanModel>) object : marsBeanObjects;
    }

    /**
     * 获取所有的加了MarsDao注解的基础数据
     *
     * @return 数据
     */
    public static List<MarsBeanClassModel> getDaoList() {
        List<MarsBeanClassModel> marsDaos = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.MARS_DAOS);
        return object != null ? (List<MarsBeanClassModel>) object : marsDaos;
    }

    /**
     * 获取所有配了定时任务的基础数据
     *
     * @return 数据
     */
    public static List<MarsTimerModel> getMarsTimersList() {
        List<MarsTimerModel> marsTimerObjects = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.MARS_TIMER_OBJECTS);

        return object != null ? (List<MarsTimerModel>) object : marsTimerObjects;
    }

    /**
     * 获取所有的加了MarsApi注解的基础数据
     *
     * @return 数据
     */
    public static List<MarsBeanClassModel> getMarsApiList() {
        List<MarsBeanClassModel> contorls = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.CONTROLLERS);

        return object != null ? (List<MarsBeanClassModel>) object : contorls;
    }

    /**
     * 获取所有的加了MarsInterceptor注解的基础数据
     *
     * @return 集合
     */
    public static List<MarsBeanClassModel> getInterceptorList() {
        List<MarsBeanClassModel> interceptors = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.INTERCEPTORS);

        return object != null ? (List<MarsBeanClassModel>) object : interceptors;
    }

    /**
     * 获取所有的加了MarsAfter注解的基础数据
     *
     * @return 集合
     */
    public static List<Class> getMarsAfterList() {
        List<Class> marsAfters = new ArrayList<>();

        Object object = constants.getAttr(MarsConstant.MARS_AFTERS);

        return object != null ? (List<Class>) object : marsAfters;
    }

    /**
     * 获取扫描出来的类
     *
     * @return 集合
     */
    public static Set<String> getSacnClassList() {
        Set<String> classList = new HashSet<>();

        Object object = constants.getAttr(MarsConstant.SCAN_ALL_CLASS);
        
        return object != null ? (Set<String>) object : classList;
    }
}
