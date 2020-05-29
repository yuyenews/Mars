package com.mars.common.test.util;

import com.mars.common.util.ReadClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 测试根据包名扫描类名
 */
public class ReadClassTest {

    @Test
    public void  testLoadClassList(){
        try {
            Map<String, Boolean> classNameMap = new HashMap<>();
            classNameMap.put("com.mars.common.test.readclass.ra.raa.TestRAA",true);
            classNameMap.put("com.mars.common.test.readclass.ra.TestRA",true);
            classNameMap.put("com.mars.common.test.readclass.rb.TestRB",true);
            classNameMap.put("com.mars.common.test.readclass.TestA",true);
            classNameMap.put("com.mars.common.test.readclass.TestB",true);

            Set<String> classNameSet = ReadClass.loadClassList("com.mars.common.test.readclass");

            Assert.assertEquals(classNameMap.size(),classNameSet.size());

            for(String className : classNameSet){
                Boolean hasEx = classNameMap.get(className);
                Assert.assertNotNull(hasEx);
                Assert.assertTrue(hasEx);
            }
        } catch (Exception e){
            Assert.fail();
        }
    }
}
