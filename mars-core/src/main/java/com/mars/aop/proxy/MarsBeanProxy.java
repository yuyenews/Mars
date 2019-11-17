package com.mars.aop.proxy;

import java.lang.reflect.Method;

import com.mars.aop.proxy.exec.ExecAop;
import com.mars.aop.proxy.exec.ExecRedisLock;
import com.mars.core.annotation.MarsAop;
import com.mars.core.annotation.RedisLock;
import com.mars.core.annotation.Traction;
import com.mars.core.model.AopModel;
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
		RedisLock redisLock = null;
		try {

			MarsAop marsAop = method.getAnnotation(MarsAop.class);
			Traction traction = method.getAnnotation(Traction.class);
			redisLock = method.getAnnotation(RedisLock.class);

			/* 校验同一个方法上不能同时存在aop和traction注解 */
			ExecAop.checkAnnot(marsAop,traction,method,cls);

			AopModel aopModel = ExecAop.getAopModel(marsAop,traction);
			if (aopModel != null) {
				c = aopModel.getCls();
			}
			if (c != null) {
				obj = c.getDeclaredConstructor().newInstance();
			}

			/* 加分布式锁 */
			Boolean hasLock = ExecRedisLock.lock(redisLock);
			if(!hasLock){
				return null;
			}

			/* 执行aop的开始方法 */
			ExecAop.startMethod(c, obj, args, aopModel);

			/* 执行方法本体 */
			Object o1 = methodProxy.invokeSuper(o, args);

			/* 执行aop的结束方法 */
			ExecAop.endMethod(c, obj, args,o1);
			return o1;
		} catch (Throwable e) {
			ExecAop.exp(c, obj, e);
			throw e;
		} finally {
			/* 解分布式锁, 如果失败了就重试，十次之后还失败，就不管了，由程序员排查问题 */
			for(int i = 0;i<10;i++){
				Boolean hasUnlock = ExecRedisLock.unlock(redisLock);
				if(hasUnlock){
					break;
				}
			}
		}
	}
}
