package com.mars.mvc.load;

import com.mars.core.load.WriteFields;
import com.mars.mvc.proxy.MvcCglibProxy;
import com.mars.core.annotation.MarsMapping;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanModel;
import com.mars.mvc.model.MarsMappingModel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载所有的controller，并完成注入
 * @author yuye
 *
 */
public class LoadController {
	
	/**
	 * 获取全局存储空间 
	 */
	private static MarsSpace constants = MarsSpace.getEasySpace();

	/**
	 * 创建controller对象，并将服务层对象注入进去
	 */
	public static void loadContrl() throws Exception{
		
		try {
			Map<String, MarsMappingModel> controlObjects = new HashMap<>();
			
			/* 获取所有的controller数据 */
			Object objs = constants.getAttr(MarsConstant.CONTROLLERS);
			List<Map<String,Object>> contorls = null;
			if(objs != null) {
				contorls = (List<Map<String,Object>>)objs;
			} else {
				return;
			}
			
			Map<String, MarsBeanModel> marsBeanObjs = getMarsBeans();
			
			for(Map<String,Object> map : contorls) {
				
				Class<?> cls = (Class<?>)map.get("className");
				
				/*
				 * 由于controller里只允许注入MarsBean，所以不需要等controller都创建好了再注入
				 * 直接 迭代一次 就给一个controller注入一次
				 */
				Object obj = iocControl(cls,marsBeanObjs);
				
				if(obj != null) {
					/* 获取controller的所有方法 */
					Method[] methods = cls.getMethods();
					for(Method method : methods) {
						MarsMapping marsMapping = method.getAnnotation(MarsMapping.class);
						if(marsMapping != null) {
							MarsMappingModel marsMappingModel = new MarsMappingModel();
							marsMappingModel.setObject(obj);
							marsMappingModel.setRequestMetohd(marsMapping.method());
							marsMappingModel.setMethod(method.getName());
							marsMappingModel.setCls(cls);
							controlObjects.put(marsMapping.value(), marsMappingModel);
						}
					}
				}
			}
			
			constants.setAttr(MarsConstant.CONTROLLER_OBJECTS, controlObjects);
		} catch (Exception e) {
			throw new Exception("加载controller并注入的时候报错",e);
		}
	}
	
	/**
	 * 往controller对象中注入easybean
	 * @param cls lei
	 * @param marsBeanObjs duix
	 * @return duix
	 */
	private static Object iocControl(Class<?> cls,Map<String, MarsBeanModel> marsBeanObjs) throws Exception{
		
		try {

			MvcCglibProxy mvcCglibProxy = new MvcCglibProxy();
			Object obj = mvcCglibProxy.getProxy(cls);

			/* 获取对象属性，完成注入 */
			WriteFields.writeFields(cls,obj,marsBeanObjs);

			return obj;
		} catch (Exception e) {
			throw new Exception("创建controller并注入的时候报错",e);
		} 
	}
	
	/**
	 * 获取所有的marsBean
	 * @return duix
	 */
	private static Map<String, MarsBeanModel> getMarsBeans() {
		Object objs = constants.getAttr(MarsConstant.MARS_BEAN_OBJECTS);
		Map<String, MarsBeanModel> easyBeanObjs = new HashMap<>();
		if(objs != null) {
			easyBeanObjs = (Map<String, MarsBeanModel>)objs;
		}
		return easyBeanObjs;
	}
}
