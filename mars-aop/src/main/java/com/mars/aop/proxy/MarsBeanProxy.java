package com.mars.aop.proxy;

import java.lang.reflect.Method;

import com.mars.core.annotation.MarsAop;
import com.mars.core.annotation.Traction;
import com.mars.core.model.AopModel;
import com.mars.core.ncfg.traction.TractionClass;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理类
 * @author yuye
 *
 */
public class MarsBeanProxy implements MethodInterceptor {

	private Enhancer enhancer;

	private Class<?> cls;

	/**
	 * 获取代理对象
	 *
	 * @param clazz bean的class
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
		Object obj = null;
		Class c = null;

		try {

			MarsAop marsAop = method.getAnnotation(MarsAop.class);
			Traction traction = method.getAnnotation(Traction.class);

			/* 校验同一个方法上不能同时存在aop和traction注解 */
			checkAnnot(marsAop,traction,method);

			AopModel aopModel = getAopModel(marsAop,traction);
			if (aopModel != null) {
				c = aopModel.getCls();
			}
			if (c != null) {
				obj = c.getDeclaredConstructor().newInstance();
			}

			startMethod(c, obj, args, aopModel);
			Object o1 = methodProxy.invokeSuper(o, args);
			endMethod(c, obj, args);
			return o1;
		} catch (Throwable e) {
			exp(c, obj, e);
			throw e;
		}
	}

	/**
	 * 方法开始前
	 * @param c 类
	 * @param obj 对象
	 * @param args 参数
	 * @param aopModel Aop实体
	 * @throws Exception 异常
	 */
	private void startMethod(Class c, Object obj, Object[] args, AopModel aopModel) throws Exception {
		if (c == null) {
			return;
		}
		if (c.getName().equals(TractionClass.getCls().getName())) {
			Method m2 = c.getDeclaredMethod("startMethod", new Class[]{Object[].class, AopModel.class});
			m2.invoke(obj, new Object[]{args, aopModel});
		} else {
			Method m2 = c.getDeclaredMethod("startMethod", new Class[]{Object[].class});
			m2.invoke(obj, new Object[]{args});
		}
	}

	/**
	 * 方法开始后
	 * @param c 类
	 * @param obj 对象
	 * @param args 参数
	 * @throws Exception 异常
	 */
	private void endMethod(Class c, Object obj, Object[] args) throws Exception {
		if (c == null) {
			return;
		}
		Method m3 = c.getDeclaredMethod("endMethod", new Class[]{Object[].class});
		m3.invoke(obj, new Object[]{args});
	}

	/**
	 * 异常
	 * @param c 类
	 * @param obj 对象
	 * @param e 异常对象
	 * @throws Exception 异常
	 */
	private void exp(Class c, Object obj, Throwable e) throws Exception {
		if (c == null) {
			return;
		}
		Method m4 = c.getDeclaredMethod("exp", new Class[]{Throwable.class});
		m4.invoke(obj, new Object[]{e});
	}

	/**
	 * 校验注解是否符合规则
	 * @param marsAop aop注解
	 * @param traction 事务监听注解
	 * @param method 被监听的方法
	 * @throws Exception 异常
	 */
	private void checkAnnot(MarsAop marsAop,Traction traction,Method method) throws Exception {
		if(marsAop != null && traction != null) {
			throw new Exception(cls.getName()+"类中的["+method.getName()+"]方法同时存在MarsAop和Traction注解");
		}
	}

	/**
	 * 获取aop实体
	 * @param marsAop aop注解
	 * @param traction 事务监听注解
	 * @return aop实体
	 */
	private AopModel getAopModel(MarsAop marsAop,Traction traction){
		AopModel aopModel = null;
		if(marsAop != null) {
			aopModel = new AopModel();
			aopModel.setCls(marsAop.className());
		} else if(traction != null) {
			aopModel = new AopModel();
			aopModel.setCls(TractionClass.getCls());
			aopModel.setTractionLevel(traction.level());
			aopModel.setExecutorType(traction.executorType());
		}
		return aopModel;
	}
}
