package com.mars.core.test.aop.aop;

import com.mars.aop.model.AopModel;
import com.mars.aop.proxy.exec.ExecAop;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试AOP执行类
 */
public class ExecAopTest {

    @Test
    public void testStartMethod(){
        try {
            AopModel aopModel = new AopModel();
            aopModel.setCls(AopTest.class);
            aopModel.setObj(new AopTest());
            ExecAop.startMethod(new Object[]{"a","b"},aopModel);
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testEndMethod(){
        try {
            AopModel aopModel = new AopModel();
            aopModel.setCls(AopTest.class);
            aopModel.setObj(new AopTest());
            ExecAop.endMethod(new Object[]{"a","b"},"返回值",aopModel);
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testExp(){
        try {
            AopModel aopModel = new AopModel();
            aopModel.setCls(AopTest.class);
            aopModel.setObj(new AopTest());
            ExecAop.exp(aopModel,new Exception("单测看一下aop异常"));
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
