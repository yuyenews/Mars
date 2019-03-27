package com.yuyenews.easy.mj.proxy;


import com.yuyenews.core.annotation.DataSource;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.util.ThreadUtil;
import com.yuyenews.jdbc.base.BaseJdbcProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 代理类
 * @author yuye
 *
 */
public class MjProxy extends BaseJdbcProxy implements MethodInterceptor {

    private EasySpace easySpace = EasySpace.getEasySpace();


    /**
     * 获取代理对象
     * @param clazz  bean的class
     * @return 对象
     */
    @Override
    public Object getProxy(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        // 设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        // 通过字节码技术动态创建子类实例
        return enhancer.create();
    }


    /**
     * 绑定代理
     * @param o
     * @param method
     * @param args
     * @param methodProxy
     * @return obj
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        /* 获取当前线程中的sqlSession */
        Object obj =  easySpace.getAttr(ThreadUtil.getThreadIdToTraction());


        /* 返回数据 */
        Object result = null;



        return result;
    }

    /**
     * 获取数据源名称
     * @param method
     * @return str
     */
    private String getDataSourceName(Method method) {
        String dataSourceName = null;
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if(dataSource != null) {
            /* 如果dao的方法上有DataSource注解，则使用注解中的数据源名称 */
            dataSourceName = dataSource.value();
        } else {
            /* 否则使用默认数据源名称 */
            dataSourceName = easySpace.getAttr("defaultDataSource").toString();
        }
        return dataSourceName;
    }
}


