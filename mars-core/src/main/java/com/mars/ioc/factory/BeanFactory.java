package com.mars.ioc.factory;

import com.mars.aop.proxy.MarsBeanProxy;
import com.mars.common.annotation.bean.MarsAop;
import com.mars.common.annotation.bean.RedisLock;
import com.mars.common.annotation.jdbc.Traction;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.load.LoadHelper;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * bean工厂
 * @author yuye
 *
 */
public class BeanFactory {

	private static MarsSpace constants = MarsSpace.getEasySpace();
	
	/**
	 * 创建bean
	 * @param className lei
	 * @return beanObject
	 *
	 * @throws Exception 异常
	 */
	public static Object createBean(Class<?> className) throws Exception {
		try {

			Object hasStart = constants.getAttr(MarsConstant.HAS_START);
			if(hasStart != null){
				throw new Exception("只有Mars才可以调用此方法，不可以手动显式调用");
			}
			Method[] methods = className.getDeclaredMethods();
			if(methods.length > 0){
				for(Method method : methods){
					MarsAop marsAop = method.getAnnotation(MarsAop.class);
					Traction traction = method.getAnnotation(Traction.class);
					RedisLock redisLock = method.getAnnotation(RedisLock.class);
					if(marsAop != null || traction != null || redisLock != null){
						/* 如果bean里面用到了AOP，就从动态代理创建对象 */
						MarsBeanProxy marsBeanProxy = new MarsBeanProxy();
						return marsBeanProxy.getProxy(className);
					}
				}
			}
			/* 如果bean里面没有用到AOP，就直接创建对象 */
			return className.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new Exception("创建["+className.getName()+"]类型的bean对象出现错误",e);
		} 
	}

	/**
	 * @param name bean名称
	 * @param cls bean类型
	 * @param <T> 类型
	 * @return bean对象
	 * @throws Exception 异常
	 */
	public static <T> T getBean(String name,Class<T> cls) throws Exception {
		try {
			Map<String, MarsBeanModel> marsBeanObjects = LoadHelper.getBeanObjectMap();
			return (T)marsBeanObjects.get(name).getObj();
		} catch (Exception e) {
			throw new Exception("找不到name为["+name+"]的bean",e);
		}
	}
}
