package com.mars.core.after;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadHelper;
import com.mars.core.load.WriteFields;
import com.mars.core.model.MarsBeanModel;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 框架启动后立刻执行
 */
public class StartAfter {

    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 框架启动后立刻执行
     *
     * @throws Exception 异常
     */
    public static void after() throws Exception {
        try {
            Object afterObjects = constants.getAttr(MarsConstant.MARS_AFTERS);
            if(afterObjects != null) {
                List<Class> easyLoads = (List<Class>)afterObjects;

                Map<String, MarsBeanModel> beanModelMap = LoadHelper.getBeanObjectMap();

                for(Class cls : easyLoads){
                    Object obj = cls.getDeclaredConstructor().newInstance();

                    /* 注入属性 */
                    WriteFields.writeFields(cls,obj,beanModelMap);

                    /* 执行after方法 */
                    Method method2 = cls.getDeclaredMethod("after");
                    method2.invoke(obj);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
