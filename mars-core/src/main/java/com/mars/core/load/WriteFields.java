package com.mars.core.load;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.annotation.MarsValue;
import com.mars.core.annotation.MarsWrite;
import com.mars.core.enums.DataType;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 属性注入
 */
public class WriteFields {

    private static Logger log = LoggerFactory.getLogger(WriteFields.class);

    /**
     * 属性注入
     *
     * @param cls             需要注入属性的类
     * @param obj             需要注入属性的对象
     * @param marsBeanObjects 要被注入的对象集合
     * @throws Exception 异常
     */
    public static void writeFields(Class cls, Object obj, Map<String, MarsBeanModel> marsBeanObjects) throws Exception {
        JSONObject config = ConfigUtil.getConfig();

        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            MarsWrite marsWrite = f.getAnnotation(MarsWrite.class);
            MarsValue marsValue = f.getAnnotation(MarsValue.class);
            if (marsWrite != null && marsValue != null) {
                throw new Exception("属性不可以同时有MarsWrite和MarsValue注解,类名:" + cls.getName());
            }

            doWrite(f,cls,obj,marsBeanObjects,marsWrite);
            doValue(f,cls,obj,config,marsValue);
        }
    }

    /**
     * 给属性注入值
     * @param f 字段
     * @param cls 类
     * @param obj 对象
     * @param marsBeanObjects bean对象集合
     * @param marsWrite 注解
     * @throws Exception 异常
     */
    private static void doWrite(Field f,Class cls,Object obj,Map<String, MarsBeanModel> marsBeanObjects,MarsWrite marsWrite) throws Exception {
        if(marsWrite == null){
            return;
        }
        String filedName = LoadHelper.getResourceName(marsWrite, f);
        MarsBeanModel beanModel = marsBeanObjects.get(filedName);
        if (beanModel != null) {
            f.setAccessible(true);
            f.set(obj, beanModel.getObj());
            log.info(cls.getName() + "的属性" + f.getName() + "注入成功");
        } else {
            throw new Exception("不存在name为" + filedName + "的MarsBean");
        }
    }

    /**
     * 给加了MarsValue注解的的属性注入值
     * @param f 字段
     * @param cls 类
     * @param obj 对象
     * @param marsValue 注解
     * @throws Exception 异常
     */
    private static void doValue(Field f,Class cls,Object obj, JSONObject config, MarsValue marsValue) throws Exception {
        if(marsValue == null){
            return;
        }
        if (!f.getType().getSimpleName().toUpperCase().equals(DataType.STRING)) {
            throw new Exception("MarsValue只能给String类型的属性注入值,类名:" + cls.getName());
        }
        f.setAccessible(true);
        String value = marsValue.value();
        Object filedValue = getValue(value,config);
        if (filedValue == null) {
            throw new Exception("无法给属性注入:" + value);
        }

        f.set(obj, filedValue.toString());
        log.info(cls.getName() + "的属性" + f.getName() + "注入成功");
    }

    /**
     * 从配置中获取要注入的值
     *
     * @param value 名称
     * @return 值
     * @throws Exception 异常
     */
    private static Object getValue(String value,JSONObject config) throws Exception {
        if (value.indexOf(".") > 0) {
            String[] values = value.split("\\.");
            if (values == null || values.length < 1) {
                throw new Exception("无法给属性注入:" + value);
            }
            if (values.length < 2) {
                return config.get(value);
            }
            JSONObject jsonObject = config;
            for (int i = 0; i < values.length; i++) {
                if (i < values.length - 1) {
                    jsonObject = jsonObject.getJSONObject(values[i]);
                    if (jsonObject == null) {
                        throw new Exception("无法给属性注入:" + value);
                    }
                } else {
                    if (jsonObject == null) {
                        throw new Exception("无法给属性注入:" + value);
                    }
                    Object filedValue = jsonObject.get(values[i]);
                    if (filedValue == null) {
                        throw new Exception("无法给属性注入:" + value);
                    }
                    return filedValue;
                }
            }
        } else {
            return config.get(value);
        }
        return null;
    }
}
