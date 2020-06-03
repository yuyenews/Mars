package com.mars.core.test.aop.traction;

import com.alibaba.fastjson.JSON;
import com.mars.aop.model.AopModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TractionTest {

    private Logger logger = LoggerFactory.getLogger(TractionTest.class);

    public void startMethod(AopModel aopModel) {
        logger.info("单测开始执行事务开启方法，事务级别:{}", JSON.toJSONString(aopModel.getTractionLevel()));
    }

    public void endMethod() {
        logger.info("单测开始执行事务提交方法");
    }

    public void exp(Throwable e) {
        logger.info("事务单测出异常",e);
    }
}
