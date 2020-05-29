package com.mars.common.test.util;

import com.mars.common.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试字符串工具类
 */
public class StringUtilTest {

    /**
     * 首字母转小写
     */
    @Test
    public void testGetFirstLowerCase(){
        String stringUtilTestStr = StringUtil.getFirstLowerCase("StringUtilTest");
        Assert.assertEquals(stringUtilTestStr,"stringUtilTest");
    }

    /**
     * 非空判断
     */
    @Test
    public void testIsNull(){
        Boolean isNull = StringUtil.isNull(null);
        Assert.assertEquals(isNull,true);

        isNull = StringUtil.isNull("notNull");
        Assert.assertEquals(isNull,false);
    }
}
