package com.mars.core.after;

import com.mars.core.load.LoadHelper;
import com.mars.core.load.WriteFields;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.model.MarsOnloadModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 框架启动后立刻执行
 */
public class StartOnLoad {

    private static final String BEFORE = "before";

    private static final String AFTER = "after";

    private static List<MarsOnloadModel> marsOnLoadModelList;

    /**
     * 框架加载bean之前执行
     * @throws Exception
     */
    public static void before() throws Exception {
        try {
            init(false);
            for(MarsOnloadModel beforeBean : marsOnLoadModelList){
                /* 执行before方法 */
                Method method2 = beforeBean.getCls().getDeclaredMethod(BEFORE);
                method2.invoke(beforeBean.getObj());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 框架启动后立刻执行
     *
     * @throws Exception 异常
     */
    public static void after() throws Exception {
        try {
            init(true);
            for(MarsOnloadModel beforeBean : marsOnLoadModelList){
                /* 执行after方法 */
                Method method2 = beforeBean.getCls().getDeclaredMethod(AFTER);
                method2.invoke(beforeBean.getObj());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 初始化onLoad对象
     * @param isAfter
     * @throws Exception
     */
    private static void init(boolean isAfter) throws Exception {
        initMarsOnLoadModels();

        if(isAfter){
            /* 如果是框架启动后，那就给loadModel注入属性 */
            Map<String, MarsBeanModel> beanModelMap = LoadHelper.getBeanObjectMap();
            List<MarsOnloadModel> newOnLoadModels = new ArrayList<>();
            for(MarsOnloadModel onloadModel : marsOnLoadModelList){
                WriteFields.writeFields(onloadModel.getCls(), onloadModel.getObj(), beanModelMap);
                newOnLoadModels.add(onloadModel);
            }
            marsOnLoadModelList = newOnLoadModels;
        }
    }

    /**
     * 初始化onload集合
     * @throws Exception
     */
    private static void initMarsOnLoadModels() throws Exception {
        if(marsOnLoadModelList != null){
            return;
        }
        marsOnLoadModelList = new ArrayList<>();
        List<Class> afterList = LoadHelper.getMarsAfterList();
        for (Class cls : afterList) {
            MarsOnloadModel marsBeanModel = new MarsOnloadModel();

            Object obj = cls.getDeclaredConstructor().newInstance();

            marsBeanModel.setObj(obj);
            marsBeanModel.setCls(cls);
            marsOnLoadModelList.add(marsBeanModel);
        }
    }
}
