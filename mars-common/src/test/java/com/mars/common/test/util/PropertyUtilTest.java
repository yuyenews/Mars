package com.mars.common.test.util;

import com.mars.common.util.PropertyUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试属性拷贝类
 */
public class PropertyUtilTest {

    @Test
    public void testCopy(){
        TestModel testModel = new TestModel();
        testModel.setId(1);
        testModel.setName("mars");

        TestModelTwo testModelTwo = PropertyUtil.copy(testModel,TestModelTwo.class);
        Assert.assertEquals(testModel.getId(),testModelTwo.getId());
        Assert.assertEquals(testModel.getName(),testModelTwo.getName());
    }
}

class TestModel {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class TestModelTwo {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
