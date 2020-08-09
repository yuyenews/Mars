package com.mars.jdbc.core.load;

import com.mars.common.annotation.jdbc.MarsDao;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsBeanClassModel;
import com.mars.core.model.MarsBeanModel;
import com.mars.jdbc.core.proxy.MjProxy;

import java.util.List;
import java.util.Map;

/**
 * 加载dao
 */
public class LoadDaos {

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 创建dao对象
     */
    public static void loadDao() throws Exception {
        try {
            List<MarsBeanClassModel> marsDaoList = LoadHelper.getDaoList();

            /* 创建bean对象，并保存起来 */
            Map<String, MarsBeanModel> marsBeanObjs = LoadHelper.getBeanObjectMap();

            for (MarsBeanClassModel marsBeanClassModel : marsDaoList) {
                Class<?> cls = marsBeanClassModel.getClassName();
                MarsDao marsDao = (MarsDao) marsBeanClassModel.getAnnotation();

                String beanName = LoadHelper.getBeanName(marsDao.value(), cls);

                if (marsBeanObjs.get(beanName) == null) {
                    MarsBeanModel beanModel = new MarsBeanModel();
                    beanModel.setName(beanName);
                    beanModel.setCls(cls);
                    beanModel.setObj(new MjProxy().getProxy(cls));
                    marsBeanObjs.put(beanName, beanModel);
                } else {
                    throw new Exception("已经存在name为[" + beanName + "]的MarsDao了");
                }
            }

            constants.setAttr(MarsConstant.MARS_BEAN_OBJECTS, marsBeanObjs);

        } catch (Exception e) {
            throw new Exception("加载MarsDao的时候出现错误", e);
        }
    }
}
