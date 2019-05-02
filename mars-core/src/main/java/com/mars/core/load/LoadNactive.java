package com.mars.core.load;

import com.mars.core.annotation.Controller;
import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.MarsDao;
import com.mars.core.annotation.MarsInterceptor;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载本地资源
 */
public class LoadNactive {

    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 加载本地bean
     * @throws Exception
     */
    public static void loadNactiveBeans() throws Exception {

        /* 加载 接受远程配置中心通知的controller */
        Class<?> cls = Class.forName(MarsConstant.REMOTE_CONFIG_CONTROLLER);
        Controller controller = cls.getAnnotation(Controller.class);
        loadController(cls, controller);
    }

    /**
     * 将所有controller存到全局存储空间
     * @param cls
     * @param controller
     */
    public static void loadController(Class<?> cls,Controller controller) {
        Object objs = constants.getAttr(MarsConstant.CONTROLLERS);
        List<Map<String,Object>> contorls = new ArrayList<>();
        if(objs != null) {
            contorls = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> contorl = new HashMap<>();
        contorl.put("className", cls);
        contorl.put("annotation", controller);
        contorls.add(contorl);
        constants.setAttr(MarsConstant.CONTROLLERS, contorls);
    }

    /**
     * 将所有easybean存到全局存储空间
     * @param cls
     * @param marsBean
     */
    public static void loadEasyBean(Class<?> cls, MarsBean marsBean) {
        Object objs = constants.getAttr(MarsConstant.EASYBEANS);
        List<Map<String,Object>> easyBeans = new ArrayList<>();
        if(objs != null) {
            easyBeans = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> eb = new HashMap<>();
        eb.put("className", cls);
        eb.put("annotation", marsBean);
        easyBeans.add(eb);
        constants.setAttr(MarsConstant.EASYBEANS, easyBeans);
    }

    /**
     * 将所有拦截器存到全局存储空间
     * @param cls
     * @param interceptor
     */
    public static void loadInterceptor(Class<?> cls, MarsInterceptor interceptor){
        Object objs = constants.getAttr(MarsConstant.INTERCEPTORS);
        List<Map<String,Object>> interceptors = new ArrayList<>();
        if(objs != null) {
            interceptors = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> eb = new HashMap<>();
        eb.put("className", cls);
        eb.put("annotation", interceptor);
        interceptors.add(eb);
        constants.setAttr(MarsConstant.INTERCEPTORS, interceptors);
    }

    /**
     * 加载dao
     * @param cls
     * @param marsDao
     */
    public static void loadDao(Class<?> cls, MarsDao marsDao){
        Object objs = constants.getAttr(MarsConstant.EASYDAOS);
        List<Map<String,Object>> easyDaos = new ArrayList<>();
        if(objs != null) {
            easyDaos = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> eb = new HashMap<>();
        eb.put("className", cls);
        eb.put("annotation", marsDao);
        easyDaos.add(eb);
        constants.setAttr(MarsConstant.EASYDAOS, easyDaos);
    }

    /**
     * 加载easyAfter
     * @param cls
     */
    public static void loadEasyAfter(Class<?> cls){
        Object objs = constants.getAttr(MarsConstant.EASYAFTERS);
        List<Class> easyLoads = new ArrayList<>();
        if(objs != null) {
            easyLoads = (List<Class>)objs;
        }
        easyLoads.add(cls);
        constants.setAttr(MarsConstant.EASYAFTERS, easyLoads);
    }
}
