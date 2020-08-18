package com.mars.mvc.resolve.proxy;

import com.mars.common.annotation.api.MarsLog;
import com.mars.mvc.logs.LogAop;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 代理类
 * @author yuye
 *
 */
public class MvcCglibProxy implements MethodInterceptor {

	private Enhancer enhancer;

	private Class cls;

	/**
	 * 获取代理对象
	 * @param clazz  bean的class
	 * @return 对象
	 */
	public Object getProxy(Class<?> clazz) {
	    this.cls = clazz;

		enhancer = new Enhancer();
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		// 通过字节码技术动态创建子类实例
		return enhancer.create();
	}
	
	
	/**
	 * 绑定代理
	 */
	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		LogAop c = null;

		MarsLog marsLog = method.getAnnotation(MarsLog.class);
		if(marsLog != null){
			c = new LogAop(cls,method.getName());
			c.startMethod(args);
		}

		try{
			Object o1 = null;
			if(cls.isInterface()){
				o1 = ExecuteRef.executeRef(cls,method,args);
			} else {
				o1 = methodProxy.invokeSuper(o,args);
			}
			if(c != null){
				c.endMethod(args,o1);
			}
			return o1;
		} catch (Throwable e) {
			if(c != null) {
				c.exp(e);
			}
			throw e;
		}
	}
}
