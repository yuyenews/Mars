package com.mars.core.test.core.after;

import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.core.after.StartAfter;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsBeanModel;
import com.mars.ioc.factory.BeanFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 测试after
 */
public class StartAfterTest {

    @Test
    public void testAfter(){
        try {

            MarsSpace marsSpace = MarsSpace.getEasySpace();

            marsSpace.remove(MarsConstant.HAS_START);

            Map<String, MarsBeanModel> beanModelMap = LoadHelper.getBeanObjectMap();
            MarsBeanModel marsBeanModel = new MarsBeanModel();
            marsBeanModel.setCls(TestBean.class);
            marsBeanModel.setName("junitTestBean");
            marsBeanModel.setObj(BeanFactory.createBean(TestBean.class));
            beanModelMap.put("junitTestBean",marsBeanModel);
            marsSpace.setAttr(MarsConstant.MARS_BEAN_OBJECTS,beanModelMap);


            List<Class> afterList = LoadHelper.getMarsAfterList();
            afterList.add(JunitAfter.class);
            marsSpace.setAttr(MarsConstant.MARS_AFTERS,afterList);

            StartAfter.after();

            marsSpace.remove(MarsConstant.MARS_AFTERS);
            marsSpace.remove(MarsConstant.MARS_BEAN_OBJECTS);
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
