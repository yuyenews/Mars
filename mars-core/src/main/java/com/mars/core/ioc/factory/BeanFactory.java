package com.mars.core.ioc.factory;

import com.mars.core.aop.proxy.MarsBeanProxy;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.load.LoadHelper;
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
	 */
	public static Object createBean(Class<?> className) throws Exception {
		try {

			Object hasStart = constants.getAttr(MarsConstant.HAS_START);
			if(hasStart != null){
				throw new Exception("只有Mars才可以调用此方法，不可以手动显式调用");
			}

			MarsBeanProxy marsBeanProxy = new MarsBeanProxy();
			return marsBeanProxy.getProxy(className);
			
		} catch (Exception e) {
			throw new Exception("创建["+className.getName()+"]类型的bean对象出现错误",e);
		} 
	}
	
	
	/**
	 * 获取bean
	 * @param name bean名称
	 * @return bean对象
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
