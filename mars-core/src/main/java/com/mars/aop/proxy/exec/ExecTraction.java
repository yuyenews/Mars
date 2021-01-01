package com.mars.aop.proxy.exec;

import com.mars.aop.base.BaseTraction;
import com.mars.aop.model.AopModel;
import com.mars.common.annotation.jdbc.Traction;
import com.mars.common.ncfg.traction.TractionFactory;

public class ExecTraction {

    /**
     * 开启事务
     * @param aopModel Aop实体
     * @throws Exception 异常
     */
    public static void beginTraction(AopModel aopModel) throws Exception {
        if (aopModel == null) {
            return;
        }
        BaseTraction baseTraction = (BaseTraction)aopModel.getObj();
        baseTraction.beginTraction(aopModel);
    }

    /**
     * 提交事务
     * @param aopModel 对象
     * @throws Exception 异常
     */
    public static void commit(AopModel aopModel) throws Exception {
        if (aopModel == null) {
            return;
        }
        BaseTraction baseTraction = (BaseTraction)aopModel.getObj();
        baseTraction.commit();
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
        BaseTraction baseTraction = (BaseTraction)aopModel.getObj();
        baseTraction.rollback(e);
    }

    /**
     * 获取aop实体
     * @param traction 事务监听注解
     * @return aop实体
     * @throws Exception 异常
     */
    public static AopModel getAopModel(Traction traction) throws Exception {
        if(traction == null){
           return null;
        }
        AopModel aopModel = new AopModel();
        aopModel.setTractionLevel(traction.level());
        aopModel.setObj(TractionFactory.getTraction());
        return aopModel;
    }
}
