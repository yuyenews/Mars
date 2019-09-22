package com.mars.mj.proxy;

import com.mars.core.annotation.DataSource;
import com.mars.jdbc.base.BaseJdbcProxy;
import com.mars.mj.annotation.MarsGet;
import com.mars.mj.annotation.MarsSelect;
import com.mars.mj.annotation.MarsUpdate;
import com.mars.mj.proxyoper.ProxyOpertion;
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
        MarsGet marsGet = method.getAnnotation(MarsGet.class);
        MarsSelect marsSelect = method.getAnnotation(MarsSelect.class);
        MarsUpdate marsUpdate = method.getAnnotation(MarsUpdate.class);

        int count = checkAnnot(marsGet,marsSelect,marsUpdate);

        if(count == 0){
            return methodProxy.invokeSuper(o, args);
        } else if(count == 1){
            Object param = checkArgs(args);
            String dataSourceName = null;
            DataSource dataSource = method.getAnnotation(DataSource.class);
            if(dataSource != null){
                dataSourceName = dataSource.value();
            }
            if(marsGet != null){
                return ProxyOpertion.get(marsGet,dataSourceName,method,param);
            }
            if(marsSelect != null){
                return ProxyOpertion.select(marsSelect,dataSourceName,method,param);
            }
            if(marsUpdate != null){
                return ProxyOpertion.update(marsUpdate,dataSourceName,param);
            }
        } else {
            throw new Exception(method.getName()+"方法上不允许有多个sql注解");
        }
        return null;
    }

    /**
     * 校验参数
     * @param args 参数
     * @return 数据
     * @throws Exception 异常
     */
    private Object checkArgs(Object[] args) throws Exception {
        if(args != null && args.length > 1){
            throw new Exception("MarsDAO的方法只允许有一个参数");
        } else if(args == null || args.length < 1){
            return null;
        }
        return args[0];
    }

    /**
     * 校验注解
     * @param marsGet 注解
     * @param marsSelect 注解
     * @param marsUpdate 注解
     * @return
     */
    private int checkAnnot(MarsGet marsGet,MarsSelect marsSelect,MarsUpdate marsUpdate){
        int count = 0;
        if(marsGet != null){
            count++;
        }
        if(marsSelect != null){
            count++;
        }
        if(marsUpdate != null){
            count++;
        }
        return count;
    }
}


