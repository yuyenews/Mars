package com.mars.mvc.proxy;

import com.mars.core.annotation.MarsReference;
import com.mars.core.annotation.enums.RefType;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 执行api引用的资源
 */
public class ExecuteRef {

    /**
     * 所有的bean对象
     */
    private static Map<String, MarsBeanModel> beanModelMap = LoadHelper.getBeanObjectMap();

    /**
     * 指定服务层的方法
     * @param method 方法
     * @param args 参数
     * @return 返回值
     * @throws Exception 异常
     */
    protected static Object executeRef(Method method, Object[] args) throws Exception {
        /* 根据注解获取到对应的bean对象实体 */
        MarsReference marsReference = method.getAnnotation(MarsReference.class);

        /* 获取bean对象的class和实例 */
        MarsBeanModel marsBeanModel = getMarsBeanModel(marsReference);
        Class<?> cls = marsBeanModel.getCls();
        Object obj = marsBeanModel.getObj();

        /* 获取引用的资源名称 */
        String refName = getRefName(method,marsReference);

        if(marsReference.refType().equals(RefType.METHOD)){
            /* 如果引用的是一个方法则执行bean里面对应的方法 */
            Object result = executeRefMethod(cls,obj,args,refName);
            if(result.equals("errorRef")){
                throw new Exception("没有找到名称为["+refName+"]的方法");
            }
            return result;
        } else {
            /* 否则就将bean里面对应的属性的值返回 */
            Field field = cls.getDeclaredField(refName);
            if(field == null){
                throw new Exception("没有找到名称为["+refName+"]的属性");
            }
            field.setAccessible(true);
            return field.get(obj);
        }
    }

    /**
     * 获取要执行的bean对象
     * @param marsReference 引用注解
     * @return bean对象实体类
     * @throws Exception 异常
     */
    private static MarsBeanModel getMarsBeanModel(MarsReference marsReference) throws Exception {
        if(marsReference == null || StringUtil.isNull(marsReference.beanName())){
            throw new Exception("没有配置MarsReference注解或者配置不正确");
        }

        MarsBeanModel marsBeanModel = beanModelMap.get(marsReference.beanName());
        if(marsBeanModel == null){
            throw new Exception("没有找到name为["+marsReference.beanName()+"]的MarsBean");
        }
        return marsBeanModel;
    }

    /**
     * 执行方法
     * @param cls 类
     * @param obj 对象
     * @param args 参数
     * @param refName 引用的资源
     * @return 返回值
     * @throws Exception 异常
     */
    private static Object executeRefMethod(Class<?> cls,Object obj,Object[] args, String refName) throws Exception {
        Method[] methods = cls.getDeclaredMethods();
        for(Method methodItem : methods){
            if(methodItem.getName().equals(refName)){
                Class<?>[] paramTypes = methodItem.getParameterTypes();
                Object[] refMethodParams =	ParamUtil.getServiceParams(paramTypes,args);
                if(refMethodParams == null){
                    return methodItem.invoke(obj);
                }
                return methodItem.invoke(obj,refMethodParams);
            }
        }
        return "errorRef";
    }

    /**
     * 获取引用的资源名称
     * @param method api的方法
     * @param marsReference 引用注解配置
     * @return
     */
    private static String getRefName(Method method,MarsReference marsReference){
        String refName = marsReference.refName();
        if(StringUtil.isNull(refName)){
            return method.getName();
        }
        return refName;
    }
}
