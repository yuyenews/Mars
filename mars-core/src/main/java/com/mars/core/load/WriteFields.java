package com.mars.core.load;

import com.mars.core.annotation.Resource;
import com.mars.core.logger.MarsLogger;
import com.mars.core.model.MarsBeanModel;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 属性注入
 */
public class WriteFields {

    private static MarsLogger log = MarsLogger.getLogger(WriteFields.class);

    /**
     * 属性注入
     * @param cls 需要注入属性的类
     * @param obj 需要注入属性的对象
     * @param marsBeanObjects 要被注入的对象集合
     * @throws Exception
     */
    public static void writeFields(Class cls, Object obj, Map<String, MarsBeanModel> marsBeanObjects) throws Exception {
        Field[] fields = cls.getDeclaredFields();
        for(Field f : fields){
            Resource resource = f.getAnnotation(Resource.class);
            if(resource!=null){
                f.setAccessible(true);

                String filedName = resource.value();
                if(filedName == null || filedName.equals("")) {
                    filedName = f.getName();
                }

                MarsBeanModel beanModel = marsBeanObjects.get(filedName);
                if(beanModel!=null){
                    f.set(obj, beanModel.getObj());
                    log.info(cls.getName()+"的属性"+f.getName()+"注入成功");
                }else{
                    throw new Exception("不存在name为"+filedName+"的MarsBean");
                }
            }
        }
    }
}
