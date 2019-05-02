package com.mars.jdbc.load;


import com.mars.core.annotation.MarsDao;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.util.StringUtil;
import com.mars.jdbc.base.BaseJdbcProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadDaos {

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 创建dao对象
     */
    public static void loadDao(BaseJdbcProxy baseProxy) throws Exception{
        try {

            Object objs = constants.getAttr(MarsConstant.EASYDAOS);
            if(objs != null) {
                List<Map<String,Object>> easyDaos = (List<Map<String,Object>>)objs;

                /* 创建bean对象，并保存起来 */
                Object objs2 = constants.getAttr(MarsConstant.EASYBEAN_OBJECTS);
                Map<String, MarsBeanModel> easyBeanObjs = new HashMap<>();
                if(objs2 != null) {
                    easyBeanObjs = (Map<String, MarsBeanModel>)objs2;
                }

                for(Map<String,Object> map : easyDaos) {
                    Class<?> cls = (Class) map.get("className");
                    MarsDao marsDao = (MarsDao)map.get("annotation");

                    String beanName = marsDao.value();
                    if(beanName == null || beanName.equals("")){
                        beanName = StringUtil.getFirstLowerCase(cls.getSimpleName());
                    }
                    if(easyBeanObjs.get(beanName) == null) {
                        MarsBeanModel beanModel = new MarsBeanModel();
                        beanModel.setName(beanName);
                        beanModel.setCls(cls);
                        beanModel.setObj(baseProxy.getProxy(cls));
                        easyBeanObjs.put(beanName, beanModel);
                    } else {
                        throw new Exception("已经存在name为["+beanName+"]的EasyDao了");
                    }
                }

                constants.setAttr(MarsConstant.EASYBEAN_OBJECTS,easyBeanObjs);
            }

        } catch (Exception e) {
            throw new Exception("加载EasyDao的时候出现错误",e);
        }
    }

}
