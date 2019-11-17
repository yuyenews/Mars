package com.mars.aop.proxy.exec;

import com.mars.aop.constant.AopConstant;
import com.mars.core.annotation.Traction;
import com.mars.aop.model.AopModel;
import com.mars.core.ncfg.traction.TractionClass;

import java.lang.reflect.Method;

public class ExecTraction {

    /**
     * 开启事务
     * @param args 参数
     * @param aopModel Aop实体
     * @throws Exception 异常
     */
    public static void beginTraction(Object[] args, AopModel aopModel) throws Exception {
        if (aopModel == null) {
            return;
        }
        Method m2 = aopModel.getCls().getDeclaredMethod(AopConstant.START_METHOD, new Class[]{Object[].class, AopModel.class});
        m2.invoke(aopModel.getObj(), new Object[]{args, aopModel});
    }

    /**
     * 提交事务
     * @param aopModel 对象
     * @param args 参数
     * @throws Exception 异常
     */
    public static void commit(Object[] args, AopModel aopModel) throws Exception {
        if (aopModel == null) {
            return;
        }
        Method m2 = aopModel.getCls().getDeclaredMethod(AopConstant.END_METHOD, new Class[]{Object[].class});
        m2.invoke(aopModel.getObj(), new Object[]{args});
    }

    /**
     * 回滚事务
     * @param aopModel 对象
     * @param e 异常对象
     * @throws Exception 异常
     */
    public static void rollback(AopModel aopModel, Throwable e) throws Exception {
        if (aopModel == null) {
            return;
        }
        Method m4 = aopModel.getCls().getDeclaredMethod(AopConstant.EXP_METHOD, new Class[]{Throwable.class});
        m4.invoke(aopModel.getObj(), new Object[]{e});
    }

    /**
     * 获取aop实体
     * @param traction 事务监听注解
     * @return aop实体
     */
    public static  AopModel getAopModel(Traction traction) throws Exception {
        if(traction != null){
            AopModel aopModel = new AopModel();
            aopModel.setCls(TractionClass.getCls());
            aopModel.setTractionLevel(traction.level());
            aopModel.setExecutorType(traction.executorType());
            aopModel.setObj(TractionClass.getCls().getDeclaredConstructor().newInstance());
            return aopModel;
        }
        return null;
    }
}
