package com.mars.common.test.util;

import com.mars.common.test.config.TestMarsConfig;
import com.mars.common.util.MarsAddressUtil;
import com.mars.common.util.MarsConfiguration;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试获取本机ip和本服务端口的的工具类
 */
public class MarsAddressUtilTest {

    /**
     * 测试获取本机ip
     */
    @Test
    public void testGetLocalIp(){
        try {
            String ipStr = MarsAddressUtil.getLocalIp();
            Assert.assertEquals("172.20.10.5",ipStr);
        } catch (Exception e){
            Assert.fail();
        }
    }

    /**
     * 测试获取配置的端口号
     */
    @Test
    public void testGetPort(){
        MarsConfiguration.loadConfig(new TestMarsConfig());
        int port = MarsAddressUtil.getPort();
        Assert.assertEquals(port,8080);
    }
}