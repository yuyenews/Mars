package com.mars.mvc.resolve;

import com.mars.mvc.proxy.MvcCglibProxy;
import com.mars.core.annotation.Controller;
import com.mars.core.annotation.MarsMapping;
import com.mars.core.annotation.Resource;
import com.mars.core.constant.EasyConstant;
import com.mars.core.constant.EasySpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.model.EasyBeanModel;
import com.mars.mvc.resolve.model.EasyMappingModel;

import java.lang.reflect.Field;
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
	
	private static MarsLogger log = MarsLogger.getLogger(LoadController.class);
	
	/**
	 * 获取全局存储空间 
	 */
	private static EasySpace constants = EasySpace.getEasySpace();

	/**
	 * 创建controller对象，并将服务层对象注入进去
	 */
	public static void loadContrl() throws Exception{
		
		try {
			Map<String,EasyMappingModel> controlObjects = new HashMap<>();
			
			/* 获取所有的controller数据 */
			Object objs = constants.getAttr(EasyConstant.CONTROLLERS);
			List<Map<String,Object>> contorls = null;
			if(objs != null) {
				contorls = (List<Map<String,Object>>)objs;
			} else {
				return;
			}
			
			Map<String,EasyBeanModel> easyBeanObjs = getEasyBeans();
			
			for(Map<String,Object> map : contorls) {
				
				Class<?> cls = (Class<?>)map.get("className");
				Controller control = (Controller)map.get("annotation");
				
				/*
				 * 由于controller里只允许注入easybean，所以不需要等controller都创建好了再注入
				 * 直接 迭代一次 就给一个controller注入一次
				 */
				Object obj = iocControl(cls,control,easyBeanObjs);
				
				if(obj != null) {
					/* 获取controller的所有方法 */
					Method[] methods = cls.getMethods();
					for(Method method : methods) {
						MarsMapping marsMapping = method.getAnnotation(MarsMapping.class);
						if(marsMapping != null) {
							EasyMappingModel easyMappingModel = new EasyMappingModel();
							easyMappingModel.setObject(obj);
							easyMappingModel.setRequestMetohd(marsMapping.method());
							easyMappingModel.setMethod(method.getName());
							easyMappingModel.setCls(cls);
							controlObjects.put(marsMapping.value(), easyMappingModel);
						}
					}
				}
			}
			
			constants.setAttr(EasyConstant.CONTROLLER_OBJECTS, controlObjects);
		} catch (Exception e) {
			throw new Exception("加载controller并注入的时候报错",e);
		}
	}
	
	/**
	 * 往controller对象中注入easybean
	 * @param cls lei
	 * @param control kongzhi
	 * @param easyBeanObjs duix
	 * @return duix
	 */
	private static Object iocControl(Class<?> cls,Controller control,Map<String,EasyBeanModel> easyBeanObjs) throws Exception{
		
		try {

			MvcCglibProxy mvcCglibProxy = new MvcCglibProxy();
			Object obj = mvcCglibProxy.getProxy(cls);

			/* 获取对象属性，完成注入 */
			Field[] fields = cls.getDeclaredFields();
			for(Field f : fields){
				Resource resource = f.getAnnotation(Resource.class);
				if(resource!=null){
					f.setAccessible(true);
					
					String filedName = resource.value();
					if(filedName == null || filedName.equals("")) {
						filedName = f.getName();
					}
					
					EasyBeanModel beanModel = easyBeanObjs.get(filedName);
					if(beanModel!=null){
						f.set(obj, beanModel.getObj());
						log.info(cls.getName()+"的属性"+f.getName()+"注入成功");
					}else{
						throw new Exception("不存在name为"+filedName+"的easyBean");
					}
				}
			}
			
			return obj;
		} catch (Exception e) {
			throw new Exception("创建controller并注入的时候报错",e);
		} 
	}
	
	/**
	 * 获取所有的easybean
	 * @return duix
	 */
	@SuppressWarnings("unchecked")
	private static Map<String,EasyBeanModel> getEasyBeans() {
		Object objs2 = constants.getAttr(EasyConstant.EASYBEAN_OBJECTS);
		Map<String,EasyBeanModel> easyBeanObjs = new HashMap<>();
		if(objs2 != null) {
			easyBeanObjs = (Map<String,EasyBeanModel>)objs2;
		}
		
		return easyBeanObjs;
	}
}
