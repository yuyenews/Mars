package com.mars.common.test.util;

import com.mars.common.util.SerializableUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * 测试序列化和反序列化
 */
public class SerializableUtilTest {

    @Test
    public void testSerializableAndDeSerializable(){
        try {
            TestSerializable testSerializable = new TestSerializable();
            testSerializable.setId(1);
            testSerializable.setName("mars");
            byte[] objByte = SerializableUtil.serialization(testSerializable);
            Assert.assertNotNull(objByte);

            TestSerializable testSerializable1 = SerializableUtil.deSerialization(objByte,TestSerializable.class);

            Assert.assertNotNull(testSerializable1);
            Assert.assertEquals(testSerializable.getId(),testSerializable1.getId());
            Assert.assertEquals(testSerializable.getName(),testSerializable1.getName());
        } catch (Exception e){
            Assert.fail();
        }
    }
}

class TestSerializable implements Serializable {
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
