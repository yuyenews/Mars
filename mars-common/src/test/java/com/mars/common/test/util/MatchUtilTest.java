package com.mars.common.test.util;

import com.mars.common.util.MatchUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试字符串匹配工具类
 */
public class MatchUtilTest {

    @Test
    public void testIsMatch(){
        String str = "test123456";

        boolean isMatch = MatchUtil.isMatch("te*",str);
        Assert.assertTrue(isMatch);

        isMatch = MatchUtil.isMatch("test*",str);
        Assert.assertTrue(isMatch);

        isMatch = MatchUtil.isMatch("*23456",str);
        Assert.assertTrue(isMatch);
    }
}
