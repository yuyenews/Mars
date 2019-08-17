package com.mars.timer.load;

import com.mars.core.annotation.MarsTimer;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsTimerModel;
import com.mars.core.load.LoadHelper;
import com.mars.ioc.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 加载所有加了MarsTimer注解的bean
 */
public class LoadMarsTimer {

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 加载所有加了MarsTimer注解的对象
     * @throws Exception
     */
    public static void loadMarsTimers() throws Exception {
        /* 获取所有的bean数据 */
        List<Map<String,Object>> marsBeansList = LoadHelper.getBeanList();

        List<MarsTimerModel> marsTimerObjects = LoadHelper.getMarsTimersList();

        for(Map<String,Object> map : marsBeansList) {

            Class<?> cls = (Class<?>)map.get("className");
            String beanName = LoadHelper.getBeanName(map,cls);
            loadMethods(cls,marsTimerObjects,beanName);
        }
    }

    /**
     * 加载加了MarsTimer注解的方法，并保存
     * @param cls
     * @param marsTimerObjects
     */
    private static void loadMethods(Class<?> cls, List<MarsTimerModel> marsTimerObjects,String beanName) throws Exception {
        Method[] methods = cls.getDeclaredMethods();
        for(Method method : methods){
            MarsTimer marsTimer = method.getAnnotation(MarsTimer.class);
            if(marsTimer != null){
                if(method.getParameterCount() > 0){
                    throw new Exception("有参数的方法不可以添加定时任务，方法名:"+cls.getName()+"."+ method.getName());
                }
                Object beanObject = BeanFactory.getBean(beanName,cls);

                MarsTimerModel marsTimerModel = new MarsTimerModel();
                marsTimerModel.setCls(cls);
                marsTimerModel.setMethodName(method.getName());
                marsTimerModel.setObj(beanObject);
                marsTimerModel.setMethod(method);
                marsTimerModel.setMarsTimer(marsTimer);
                marsTimerObjects.add(marsTimerModel);
            }
        }
        /* 保险起见，重新插入数据 */
        constants.setAttr(MarsConstant.MARS_TIMER_OBJECTS, marsTimerObjects);
    }
}
