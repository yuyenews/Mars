package com.mars.mvc.load;

import com.mars.core.annotation.enums.ReqMethod;
import com.mars.core.load.LoadHelper;
import com.mars.core.load.WriteFields;
import com.mars.core.model.MarsBeanClassModel;
import com.mars.mvc.proxy.MvcCglibProxy;
import com.mars.core.annotation.RequestMethod;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.model.MarsBeanModel;
import com.mars.mvc.model.MarsMappingModel;
import com.mars.mvc.util.ParamAndResult;
import com.mars.netty.par.factory.ParamAndResultFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载所有的MarsApi，并完成注入
 * @author yuye
 *
 */
public class LoadMarsApi {
	
	/**
	 * 获取全局存储空间 
	 */
	private static MarsSpace constants = MarsSpace.getEasySpace();

	/**
	 * 创建MarsApi对象，并将服务层对象注入进去
	 */
	public static void loadMarsApis() throws Exception{
		
		try {
			/* 指定 处理参数和响应的对象实例 */
			ParamAndResultFactory.setBaseParamAndResult(new ParamAndResult());

			/* 创建一个Map用来储存API信息 */
			Map<String, MarsMappingModel> marsApiObjects = new HashMap<>();
			
			/* 获取所有的MarsApi数据 */
			List<MarsBeanClassModel> marsApiList = LoadHelper.getMarsApiList();

			/* 获取所有的marsBean */
			Map<String, MarsBeanModel> marsBeanObjs = LoadHelper.getBeanObjectMap();
			
			for(MarsBeanClassModel marsBeanClassModel : marsApiList) {
				
				Class<?> cls = marsBeanClassModel.getClassName();
				
				/*
				 * 由于MarsApi里只允许注入MarsBean，所以不需要等MarsApi都创建好了再注入
				 * 直接 迭代一次 就给一个MarsApi注入一次
				 */
				Object obj = iocMarsApi(cls,marsBeanObjs);

				/* 获取MarsApi的所有方法 */
				Method[] methods = cls.getMethods();
				for(Method method : methods) {
					if(method.getDeclaringClass().equals(cls)){
						/* 校验方法 */
						checkMethodName(marsApiObjects,cls,method);

						/* 创建映射对象 */
						MarsMappingModel marsMappingModel = new MarsMappingModel();
						marsMappingModel.setObject(obj);
						marsMappingModel.setMethod(method.getName());
						marsMappingModel.setCls(cls);
						marsMappingModel.setExeMethod(method);
						marsMappingModel.setReqMethod(getReqMethod(method));

						/* 保存映射对象 */
						marsApiObjects.put(method.getName(), marsMappingModel);
					}
				}
			}
			
			constants.setAttr(MarsConstant.CONTROLLER_OBJECTS, marsApiObjects);
		} catch (Exception e) {
			throw new Exception("加载MarsApi并注入的时候报错",e);
		}
	}
	
	/**
	 * 往MarsApi对象中注入bean
	 * @param cls 类
	 * @param marsBeanObjs 对象
	 * @return 对象
	 */
	private static Object iocMarsApi(Class<?> cls, Map<String, MarsBeanModel> marsBeanObjs) throws Exception{
		
		try {
			MvcCglibProxy mvcCglibProxy = new MvcCglibProxy();
			Object obj = mvcCglibProxy.getProxy(cls);

			if(!cls.isInterface()){
				/* 获取对象属性，完成注入 */
				WriteFields.writeFields(cls,obj,marsBeanObjs);
			}
			return obj;
		} catch (Exception e) {
			throw new Exception("创建MarsApi并注入的时候报错",e);
		} 
	}

	/**
	 * 获取接口的请求方式
	 * @param method
	 * @return
	 */
	private static ReqMethod getReqMethod(Method method){
		RequestMethod requestMethod = method.getAnnotation(RequestMethod.class);
		if(requestMethod != null){
			return requestMethod.value();
		}
		return ReqMethod.GET;
	}

	/**
	 * 校验MarsApi里的方法名是否全局唯一
	 * @param controlObjects MarsApi对象
	 * @param cls MarsApi类
	 * @param method MarsApi中的方法
	 * @throws Exception 异常
	 */
	private static void checkMethodName(Map<String, MarsMappingModel> controlObjects, Class<?> cls, Method method) throws Exception {
		MarsMappingModel marsMappingModel = controlObjects.get(method.getName());
		if (marsMappingModel != null) {
			String yName = marsMappingModel.getCls().getName();
			String xName = cls.getName();
			throw new Exception("方法名发生冲突[" + yName + "." + marsMappingModel.getMethod() + "," + xName + "." + method.getName() + "]");
		}
	}
}
