package com.mars.core.after;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;

import java.lang.reflect.Method;
import java.util.List;

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
            Object objs = constants.getAttr(MarsConstant.MARS_AFTERS);
            if(objs != null) {
                List<Class> easyLoads = (List<Class>)objs;

                for(Class cls : easyLoads){
                    Object obj = cls.getDeclaredConstructor().newInstance();
                    Method method2 = cls.getDeclaredMethod("after");
                    method2.invoke(obj);
                }
            }
        } catch (Exception e) {
            throw e;
        }

    }
}
