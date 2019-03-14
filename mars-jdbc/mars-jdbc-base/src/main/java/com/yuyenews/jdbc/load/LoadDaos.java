package com.yuyenews.jdbc.load;


import com.yuyenews.core.annotation.EasyDao;
import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.model.EasyBeanModel;
import com.yuyenews.core.util.StringUtil;
import com.yuyenews.jdbc.base.BaseJdbcProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadDaos {

    /**
     * 获取全局存储空间
     */
    private static EasySpace constants = EasySpace.getEasySpace();

    /**
     * 创建dao对象
     */
    public static void loadDao(BaseJdbcProxy baseProxy) throws Exception{
        try {

            Object objs = constants.getAttr(EasyConstant.EASYDAOS);
            if(objs != null) {
                List<Map<String,Object>> easyDaos = (List<Map<String,Object>>)objs;

                /* 创建bean对象，并保存起来 */
                Object objs2 = constants.getAttr(EasyConstant.EASYBEAN_OBJECTS);
                Map<String,EasyBeanModel> easyBeanObjs = new HashMap<>();
                if(objs2 != null) {
                    easyBeanObjs = (Map<String,EasyBeanModel>)objs2;
                }

                for(Map<String,Object> map : easyDaos) {
                    Class<?> cls = (Class) map.get("className");
                    EasyDao easyDao = (EasyDao)map.get("annotation");

                    String beanName = easyDao.value();
                    if(beanName == null || beanName.equals("")){
                        beanName = StringUtil.getFirstLowerCase(cls.getSimpleName());
                    }
                    if(easyBeanObjs.get(beanName) == null) {
                        EasyBeanModel beanModel = new EasyBeanModel();
                        beanModel.setName(beanName);
                        beanModel.setCls(cls);
                        beanModel.setObj(baseProxy.getProxy(cls));
                        easyBeanObjs.put(beanName, beanModel);
                    } else {
                        throw new Exception("已经存在name为["+beanName+"]的EasyDao了");
                    }
                }

                constants.setAttr(EasyConstant.EASYBEAN_OBJECTS,easyBeanObjs);
            }

        } catch (Exception e) {
            throw new Exception("加载EasyDao的时候出现错误",e);
        }
    }

}
