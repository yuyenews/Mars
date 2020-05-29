package com.mars.common.test.util;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.test.config.TestMarsConfig;
import com.mars.common.util.MarsConfiguration;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试配置类加载
 */
public class MarsConfigurationTest {

    /**
     * 测试配置类加载和读取
     */
    @Test
    public void testLoadAndReadConfig(){
        TestMarsConfig testMarsConfig = new TestMarsConfig();
        MarsConfiguration.loadConfig(testMarsConfig);

        MarsConfig marsConfig = MarsConfiguration.getConfig();
        Assert.assertEquals(testMarsConfig,marsConfig);
    }
}
