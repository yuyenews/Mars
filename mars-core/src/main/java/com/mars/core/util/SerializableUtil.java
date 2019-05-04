package com.mars.core.util;

import java.io.*;
import java.util.Map;

/**
 * 序列化与反序列化工具类
 */
public class SerializableUtil {

    /**
     * 将对象序列化成二进制流
     * @param obj 对象
     * @return 对象
     */
    public static byte[] serialization(Object obj) throws Exception {
        try {
            if(obj instanceof Serializable){
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
                objectOutputStream.writeObject(obj);
                return os.toByteArray();
            } else {
                throw new Exception("参数对象必须实现Serializable接口");
            }
        } catch (Exception e){
            throw new Exception("将参数序列化成二进制流，出现异常",e);
        }
    }

    /**
     * 将二进制流反序列化成对象
     * @param by 对象
     * @param cls 类型
     * @param <T> 对象
     * @return 对象
     * @throws Exception 异常
     */
    public static <T> T deSerialization(byte[] by,Class<T> cls) throws Exception {
        try {
            ByteArrayInputStream bs = new ByteArrayInputStream(by);
            ObjectInputStream objectInputStream = new ObjectInputStream(bs);
            return (T)objectInputStream.readObject();
        } catch (Exception e){
            throw new Exception("将二进制流反序列化成参数，出现异常",e);
        }
    }
}
