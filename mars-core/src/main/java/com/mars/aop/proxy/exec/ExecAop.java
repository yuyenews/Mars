package com.mars.aop.proxy.exec;

import com.mars.aop.constant.AopConstant;
import com.mars.core.annotation.MarsAop;
import com.mars.core.annotation.Traction;
import com.mars.core.model.AopModel;
import com.mars.core.ncfg.traction.TractionClass;

import java.lang.reflect.Method;

/**
 * 执行AOP
 */
public class ExecAop {

    /**
     * 方法开始前
     * @param c 类
     * @param obj 对象
     * @param args 参数
     * @param aopModel Aop实体
     * @throws Exception 异常
     */
    public static void startMethod(Class c, Object obj, Object[] args, AopModel aopModel) throws Exception {
        if (c == null) {
            return;
        }
        if (c.getName().equals(TractionClass.getCls().getName())) {
            Method m2 = c.getDeclaredMethod(AopConstant.START_METHOD, new Class[]{Object[].class, AopModel.class});
            m2.invoke(obj, new Object[]{args, aopModel});
        } else {
            Method m2 = c.getDeclaredMethod(AopConstant.START_METHOD, new Class[]{Object[].class});
            m2.invoke(obj, new Object[]{args});
        }
    }

    /**
     * 方法开始后
     * @param c 类
     * @param obj 对象
     * @param args 参数
     * @throws Exception 异常
     */
    public static void endMethod(Class c, Object obj, Object[] args, Object result) throws Exception {
        if (c == null) {
            return;
        }
        if (c.getName().equals(TractionClass.getCls().getName())) {
            Method m2 = c.getDeclaredMethod(AopConstant.END_METHOD, new Class[]{Object[].class});
            m2.invoke(obj, new Object[]{args});
        } else {
            Method m2 = c.getDeclaredMethod(AopConstant.END_METHOD, new Class[]{Object[].class, Object.class});
            m2.invoke(obj, new Object[]{args,result});
        }
    }

    /**
     * 异常
     * @param c 类
     * @param obj 对象
     * @param e 异常对象
     * @throws Exception 异常
     */
    public static void exp(Class c, Object obj, Throwable e) throws Exception {
        if (c == null) {
            return;
        }
        Method m4 = c.getDeclaredMethod(AopConstant.EXP_METHOD, new Class[]{Throwable.class});
        m4.invoke(obj, new Object[]{e});
    }

    /**
     * 校验注解是否符合规则
     * @param marsAop aop注解
     * @param traction 事务监听注解
     * @param method 被监听的方法
     * @throws Exception 异常
     */
    public static void checkAnnot(MarsAop marsAop, Traction traction, Method method,Class<?> cls) throws Exception {
        if(marsAop != null && traction != null) {
            throw new Exception(cls.getName()+"类中的["+method.getName()+"]方法同时存在MarsAop和Traction注解");
        }
    }

    /**
     * 获取aop实体
     * @param marsAop aop注解
     * @param traction 事务监听注解
     * @return aop实体
     */
    public static  AopModel getAopModel(MarsAop marsAop,Traction traction){
        AopModel aopModel = null;
        if(marsAop != null) {
            aopModel = new AopModel();
            aopModel.setCls(marsAop.className());
        } else if(traction != null) {
            aopModel = new AopModel();
            aopModel.setCls(TractionClass.getCls());
            aopModel.setTractionLevel(traction.level());
            aopModel.setExecutorType(traction.executorType());
        }
        return aopModel;
    }
}
