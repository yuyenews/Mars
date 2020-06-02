package com.mars.core.test.aop.aop;

import com.alibaba.fastjson.JSON;
import com.mars.aop.base.BaseAop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AopTest implements BaseAop {

    private Logger logger = LoggerFactory.getLogger(AopTest.class);

    @Override
    public void startMethod(Object[] args) {
        logger.info("AOP单测开始执行startMethod，方法参数:{}",JSON.toJSONString(args));
    }

    @Override
    public void endMethod(Object[] args, Object result) {
        logger.info("AOP单测开始执行endMethod，方法参数:{}，方法返回值:{}",JSON.toJSONString(args), result);
    }

    @Override
    public void exp(Throwable e) {
        logger.error("AOP单测出异常",e);
    }
}
