package com.mars.core.load;

import com.mars.core.annotation.Controller;
import com.mars.core.annotation.MarsBean;
import com.mars.core.annotation.MarsDao;
import com.mars.core.annotation.MarsInterceptor;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanClassModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 加载本地资源
 */
public class LoadBeans {

    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 加载本地bean
     * @throws Exception 异常
     */
    public static Set<String> loadNativeBeans() throws Exception {
        Set<String> navClassList = new HashSet<>();

        /* 加载 接受远程配置中心通知的controller */
        navClassList.add("com.mars.mvc.remote.controller.RemoteConfigController");

        return navClassList;
    }

    /**
     * 将所有controller存到全局存储空间
     * @param cls 类型
     * @param controller 注解
     */
    public static void loadController(Class<?> cls,Controller controller) {

        List<MarsBeanClassModel> contorls = LoadHelper.getControllerList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(controller);

        contorls.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.CONTROLLERS, contorls);
    }

    /**
     * 将所有marsBean存到全局存储空间
     * @param cls 类型
     * @param marsBean 注解
     */
    public static void loadEasyBean(Class<?> cls, MarsBean marsBean) {

        List<MarsBeanClassModel> marsBeans = LoadHelper.getBeanList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(marsBean);

        marsBeans.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.MARS_BEANS, marsBeans);
    }

    /**
     * 将所有拦截器存到全局存储空间
     * @param cls 类型
     * @param interceptor 注解
     */
    public static void loadInterceptor(Class<?> cls, MarsInterceptor interceptor){

        List<MarsBeanClassModel> interceptors = LoadHelper.getInterceptorList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(interceptor);

        interceptors.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.INTERCEPTORS, interceptors);
    }

    /**
     * 加载dao
     * @param cls 类型
     * @param marsDao 注解
     */
    public static void loadDao(Class<?> cls, MarsDao marsDao){

        List<MarsBeanClassModel> marsDaos = LoadHelper.getDaoList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(marsDao);

        marsDaos.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.MARS_DAOS, marsDaos);
    }

    /**
     * 加载marsAfter
     * @param cls 类型
     */
    public static void loadMarsAfter(Class<?> cls){
        List<Class> marsAfters = LoadHelper.getMarsAfterList();
        marsAfters.add(cls);
        constants.setAttr(MarsConstant.MARS_AFTERS, marsAfters);
    }
}
