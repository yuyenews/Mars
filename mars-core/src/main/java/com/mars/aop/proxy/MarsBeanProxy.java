package com.mars.aop.proxy;

import java.lang.reflect.Method;

import com.mars.aop.proxy.exec.ExecAop;
import com.mars.aop.proxy.exec.ExecRedisLock;
import com.mars.aop.proxy.exec.ExecTraction;
import com.mars.core.annotation.MarsAop;
import com.mars.core.annotation.RedisLock;
import com.mars.core.annotation.Traction;
import com.mars.aop.model.AopModel;
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

	/**
	 * 获取代理对象
	 *
	 * @param clazz bean的class
	 * @return 对象
	 */
	public Object getProxy(Class<?> clazz) {
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
		AopModel aopModel = null;
		AopModel tractionModel = null;
		RedisLock redisLock = null;
		Boolean hasLock = false;
		try {

			MarsAop marsAop = method.getAnnotation(MarsAop.class);
			Traction traction = method.getAnnotation(Traction.class);
			redisLock = method.getAnnotation(RedisLock.class);

			tractionModel = ExecTraction.getAopModel(traction);
			aopModel = ExecAop.getAopModel(marsAop);


			/* 加分布式锁 */
			hasLock = ExecRedisLock.lock(redisLock);
			if(!hasLock){
				return null;
			}

			/* 开启事务 */
			ExecTraction.beginTraction(tractionModel);

			/* 执行aop的开始方法 */
			ExecAop.startMethod(args, aopModel);

			/* 执行方法本体 */
			Object o1 = methodProxy.invokeSuper(o, args);

			/* 执行aop的结束方法 */
			ExecAop.endMethod(args,o1,aopModel);

			/* 提交事务 */
			ExecTraction.commit(tractionModel);
			return o1;
		} catch (Throwable e) {
			/* 回滚事务 */
			ExecTraction.rollback(tractionModel,e);
			/* AOP处理异常 */
			ExecAop.exp(aopModel, e);
			throw e;
		} finally {
			/* 解分布式锁, 如果失败了就重试，十次之后还失败，就不管了，由程序员排查问题 */
			if(hasLock){
				for(int i = 0;i<10;i++){
					Boolean hasUnlock = ExecRedisLock.unlock(redisLock);
					if(hasUnlock){
						break;
					}
				}
			}
		}
	}
}
