package com.mars.core.test.aop.traction;

import com.mars.aop.model.AopModel;
import com.mars.aop.proxy.exec.ExecTraction;
import com.mars.common.annotation.enums.TractionLevel;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试事务执行类
 */
public class ExecTractionTest {

    @Test
    public void testBeginTraction(){
        try {
            AopModel aopModel = new AopModel();
            aopModel.setCls(TractionTest.class);
            aopModel.setObj(new TractionTest());
            aopModel.setTractionLevel(TractionLevel.READ_UNCOMMITTED);
            ExecTraction.beginTraction(aopModel);

            aopModel.setTractionLevel(TractionLevel.READ_COMMITTED);
            ExecTraction.beginTraction(aopModel);

            aopModel.setTractionLevel(TractionLevel.SERIALIZABLE);
            ExecTraction.beginTraction(aopModel);
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCommit(){
        try {
            AopModel aopModel = new AopModel();
            aopModel.setCls(TractionTest.class);
            aopModel.setObj(new TractionTest());
            ExecTraction.commit(aopModel);
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testRollback(){
        try {
            AopModel aopModel = new AopModel();
            aopModel.setCls(TractionTest.class);
            aopModel.setObj(new TractionTest());
            ExecTraction.rollback(aopModel,new Exception("单测看一下事务异常"));
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
