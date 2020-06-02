package com.mars.core.test.ioc;

import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.core.load.LoadBeans;
import com.mars.core.load.LoadClass;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.test.core.load.junitbean.JunitIocMarsBean;
import com.mars.core.test.core.load.junitbean.JunitMarsBean;
import com.mars.ioc.load.LoadMarsBean;
import com.mars.redis.lock.MarsRedisLock;
import com.mars.redis.template.MarsRedisTemplate;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试bean创建
 */
public class LoadMarsBeanTest {

    @Test
    public void testLoadBean(){
        try {
            MarsSpace marsSpace = MarsSpace.getEasySpace();

            List<String> packageList = new ArrayList<>();
            packageList.add("com.mars.core.test.core.load.junitbean");
            LoadClass.scanClass(packageList);

            LoadBeans.loadBeans();

            LoadMarsBean.loadBean();

            Map<String, MarsBeanModel> marsBeanObjects = LoadHelper.getBeanObjectMap();

            MarsBeanModel junitIocMarsBean = marsBeanObjects.get("junitIocMarsBean");
            Assert.assertNotNull(junitIocMarsBean);
            Assert.assertEquals(junitIocMarsBean.getCls(), JunitIocMarsBean.class);
            Assert.assertEquals(junitIocMarsBean.getName(),"junitIocMarsBean");
            Assert.assertNotNull(junitIocMarsBean.getObj());

            MarsBeanModel junitMarsBean = marsBeanObjects.get("junitMarsBean");
            Assert.assertNotNull(junitMarsBean);

            Assert.assertEquals(junitMarsBean.getCls(),JunitMarsBean.class);
            Assert.assertEquals(junitMarsBean.getName(),"junitMarsBean");
            Assert.assertNotNull(junitMarsBean.getObj());

            /* 校验属性是否注入成功 */
            JunitMarsBean junitMarsBeanObj = (JunitMarsBean)junitMarsBean.getObj();
            Assert.assertNotNull(junitMarsBeanObj.getJunitIocMarsBean());

            marsSpace.remove(MarsConstant.CONTROLLERS);
            marsSpace.remove(MarsConstant.MARS_AFTERS);
            marsSpace.remove(MarsConstant.MARS_BEANS);
            marsSpace.remove(MarsConstant.MARS_DAOS);
            marsSpace.remove(MarsConstant.INTERCEPTORS);

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
