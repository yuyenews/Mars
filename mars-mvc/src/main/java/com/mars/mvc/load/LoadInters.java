package com.mars.mvc.load;

import com.mars.core.annotation.MarsInterceptor;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadHelper;
import com.mars.core.load.WriteFields;
import com.mars.core.model.MarsBeanClassModel;
import com.mars.mvc.model.MarsInterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载拦截器
 */
public class LoadInters {

    private static Logger log = LoggerFactory.getLogger(LoadInters.class);

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 创建所有拦截器对象
     */
    public static void loadIntersList() {
        try {
            List<MarsInterModel> list = new ArrayList<>();

            List<MarsBeanClassModel> interceptors = LoadHelper.getInterceptorList();

            for (MarsBeanClassModel marsBeanClassModel : interceptors) {

                MarsInterceptor marsInterceptor = (MarsInterceptor) marsBeanClassModel.getAnnotation();
                String pattern = marsInterceptor.pattern();
                Class cls = marsBeanClassModel.getClassName();

                MarsInterModel marsInterModel = new MarsInterModel();
                marsInterModel.setCls(cls);
                marsInterModel.setObj(cls.getDeclaredConstructor().newInstance());
                marsInterModel.setPattern(pattern);

                /* 给拦截器注入属性 */
                WriteFields.writeFields(cls, marsInterModel.getObj(), LoadHelper.getBeanObjectMap());

                list.add(marsInterModel);
            }
            constants.setAttr(MarsConstant.INTERCEPTOR_OBJECTS, list);

        } catch (Exception e) {
            log.error("读取拦截器报错", e);
        }
    }
}
