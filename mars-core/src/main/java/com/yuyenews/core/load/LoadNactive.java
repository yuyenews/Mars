package com.yuyenews.core.load;

import com.yuyenews.core.annotation.Controller;
import com.yuyenews.core.annotation.EasyBean;
import com.yuyenews.core.annotation.EasyDao;
import com.yuyenews.core.annotation.EasyInterceptor;
import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.constant.EasySpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载本地资源
 */
public class LoadNactive {

    private static EasySpace constants = EasySpace.getEasySpace();

    /**
     * 加载本地bean
     * @throws Exception
     */
    public static void loadNactiveBeans() throws Exception {

        /* 加载 接受远程配置中心通知的controller */
        Class<?> cls = Class.forName(EasyConstant.REMOTE_CONFIG_CONTROLLER);
        Controller controller = cls.getAnnotation(Controller.class);
        loadController(cls, controller);

    }


    /**
     * 将所有controller存到全局存储空间
     * @param cls
     * @param controller
     */
    public static void loadController(Class<?> cls,Controller controller) {
        Object objs = constants.getAttr(EasyConstant.CONTROLLERS);
        List<Map<String,Object>> contorls = new ArrayList<>();
        if(objs != null) {
            contorls = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> contorl = new HashMap<>();
        contorl.put("className", cls);
        contorl.put("annotation", controller);
        contorls.add(contorl);
        constants.setAttr(EasyConstant.CONTROLLERS, contorls);
    }

    /**
     * 将所有easybean存到全局存储空间
     * @param cls
     * @param easyBean
     */
    public static void loadEasyBean(Class<?> cls, EasyBean easyBean) {
        Object objs = constants.getAttr(EasyConstant.EASYBEANS);
        List<Map<String,Object>> easyBeans = new ArrayList<>();
        if(objs != null) {
            easyBeans = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> eb = new HashMap<>();
        eb.put("className", cls);
        eb.put("annotation", easyBean);
        easyBeans.add(eb);
        constants.setAttr(EasyConstant.EASYBEANS, easyBeans);
    }

    /**
     * 将所有拦截器存到全局存储空间
     * @param cls
     * @param interceptor
     */
    public static void loadInterceptor(Class<?> cls, EasyInterceptor interceptor){
        Object objs = constants.getAttr(EasyConstant.INTERCEPTORS);
        List<Map<String,Object>> interceptors = new ArrayList<>();
        if(objs != null) {
            interceptors = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> eb = new HashMap<>();
        eb.put("className", cls);
        eb.put("annotation", interceptor);
        interceptors.add(eb);
        constants.setAttr(EasyConstant.INTERCEPTORS, interceptors);
    }

    /**
     * 加载dao
     * @param cls
     * @param easyDao
     */
    public static void loadDao(Class<?> cls, EasyDao easyDao){
        Object objs = constants.getAttr(EasyConstant.EASYDAOS);
        List<Map<String,Object>> easyDaos = new ArrayList<>();
        if(objs != null) {
            easyDaos = (List<Map<String,Object>>)objs;
        }
        Map<String,Object> eb = new HashMap<>();
        eb.put("className", cls);
        eb.put("annotation", easyDao);
        easyDaos.add(eb);
        constants.setAttr(EasyConstant.EASYDAOS, easyDaos);
    }

    /**
     * 加载easyAfter
     * @param cls
     */
    public static void loadEasyAfter(Class<?> cls){
        Object objs = constants.getAttr(EasyConstant.EASYAFTERS);
        List<Class> easyLoads = new ArrayList<>();
        if(objs != null) {
            easyLoads = (List<Class>)objs;
        }
        easyLoads.add(cls);
        constants.setAttr(EasyConstant.EASYAFTERS, easyLoads);
    }
}
