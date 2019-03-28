package com.yuyenews.mj.proxy;

import com.yuyenews.core.constant.EasySpace;
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
        /* TODO(后面升级) */
        return methodProxy.invokeSuper(o, args);
    }
}


