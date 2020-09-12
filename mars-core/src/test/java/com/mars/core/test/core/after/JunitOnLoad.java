package com.mars.core.test.core.after;

import com.mars.common.annotation.bean.MarsWrite;
import com.mars.common.base.BaseOnLoad;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JunitOnLoad implements BaseOnLoad {

    private Logger logger = LoggerFactory.getLogger(JunitOnLoad.class);

    @MarsWrite("junitTestBean")
    private TestBean testBean;

    @Override
    public void after() throws Exception {
        logger.info("执行了After方法，testBean的值:{}",testBean);
        Assert.assertNotNull(testBean);
    }
}
