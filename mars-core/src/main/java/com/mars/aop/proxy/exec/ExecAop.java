package com.mars.aop.proxy.exec;

import com.mars.aop.constant.AopConstant;
import com.mars.core.annotation.MarsAop;
import com.mars.aop.model.AopModel;

import java.lang.reflect.Method;

/**
 * 执行AOP
 */
public class ExecAop {

    /**
     * 方法开始前
     * @param args 参数
     * @param aopModel Aop实体
     * @throws Exception 异常
     */
    public static void startMethod(Object[] args, AopModel aopModel) throws Exception {
        if (aopModel == null) {
            return;
        }
        Method m2 = aopModel.getCls().getDeclaredMethod(AopConstant.START_METHOD, new Class[]{Object[].class});
        m2.invoke(aopModel.getObj(), new Object[]{args});
    }

    /**
     * 方法开始后
     * @param args 参数
     * @param result 参数
     * @param aopModel 对象
     * @throws Exception 异常
     */
    public static void endMethod(Object[] args, Object result, AopModel aopModel) throws Exception {
        if (aopModel == null) {
            return;
        }
        Method m2 = aopModel.getCls().getDeclaredMethod(AopConstant.END_METHOD, new Class[]{Object[].class, Object.class});
        m2.invoke(aopModel.getObj(), new Object[]{args,result});
    }

    /**
     * 异常
     * @param aopModel 对象
     * @param e 异常对象
     * @throws Exception 异常
     */
    public static void exp(AopModel aopModel, Throwable e) throws Exception {
        if (aopModel == null) {
            return;
        }
        Method m4 = aopModel.getCls().getDeclaredMethod(AopConstant.EXP_METHOD, new Class[]{Throwable.class});
        m4.invoke(aopModel.getObj(), new Object[]{e});
    }

    /**
     * 获取aop实体
     * @param marsAop aop注解
     * @return aop实体
     */
    public static AopModel getAopModel(MarsAop marsAop) throws Exception {
        if(marsAop == null) {
            return null;
        }
        AopModel aopModel = new AopModel();
        aopModel.setCls(marsAop.className());
        aopModel.setObj(marsAop.className().getDeclaredConstructor().newInstance());
        return aopModel;
    }
}
