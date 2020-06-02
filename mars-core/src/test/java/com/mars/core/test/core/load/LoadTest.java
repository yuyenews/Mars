package com.mars.core.test.core.load;

import com.mars.common.annotation.api.MarsApi;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.core.load.LoadBeans;
import com.mars.core.load.LoadClass;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsBeanClassModel;
import com.mars.core.test.core.load.junitbean.*;
import com.mars.redis.lock.MarsRedisLock;
import com.mars.redis.template.MarsRedisTemplate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * bean扫描测试
 */
public class LoadTest {

    @Test
    public void testLoad(){
        try {

            MarsSpace marsSpace = MarsSpace.getEasySpace();

            List<String> packageList = new ArrayList<>();
            packageList.add("com.mars.core.test.core.load.junitbean");
            LoadClass.scanClass(packageList);

            LoadBeans.loadBeans();

            List<MarsBeanClassModel> marsApiList = LoadHelper.getMarsApiList();
            for(MarsBeanClassModel api : marsApiList){
                Assert.assertEquals(api.getClassName(), JunitMarsApi.class);
                Assert.assertNotNull(api.getAnnotation());
            }
            marsSpace.remove(MarsConstant.CONTROLLERS);

            List<Class> afterList = LoadHelper.getMarsAfterList();
            for(Class afterCls : afterList){
                Assert.assertEquals(afterCls, JunitMarsAfter.class);
            }
            marsSpace.remove(MarsConstant.MARS_AFTERS);

            List<MarsBeanClassModel> marsBeanClassModelList = LoadHelper.getBeanList();
            for(MarsBeanClassModel bean : marsBeanClassModelList){
                if(bean.getClassName().equals(MarsRedisTemplate.class)){
                    continue;
                }
                if(bean.getClassName().equals(MarsRedisLock.class)){
                    continue;
                }
                if(bean.getClassName().equals(JunitMarsBean.class)){
                    Assert.assertEquals(bean.getClassName(), JunitMarsBean.class);
                    Assert.assertNotNull(bean.getAnnotation());
                }
                if(bean.getClassName().equals(JunitIocMarsBean.class)){
                    Assert.assertEquals(bean.getClassName(), JunitIocMarsBean.class);
                    Assert.assertNotNull(bean.getAnnotation());
                }
            }
            marsSpace.remove(MarsConstant.MARS_BEANS);

            List<MarsBeanClassModel> daoList = LoadHelper.getDaoList();
            for(MarsBeanClassModel dao : daoList){
                Assert.assertEquals(dao.getClassName(), JunitMarsDao.class);
                Assert.assertNotNull(dao.getAnnotation());
            }
            marsSpace.remove(MarsConstant.MARS_DAOS);

            List<MarsBeanClassModel> interList = LoadHelper.getInterceptorList();
            for(MarsBeanClassModel inter : interList){
                Assert.assertEquals(inter.getClassName(), JunitMarsInterceptor.class);
                Assert.assertNotNull(inter.getAnnotation());
            }
            marsSpace.remove(MarsConstant.INTERCEPTORS);

        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
