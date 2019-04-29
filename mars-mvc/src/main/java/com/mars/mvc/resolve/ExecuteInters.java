package com.mars.mvc.resolve;

import com.alibaba.fastjson.JSONObject;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.core.annotation.MarsInterceptor;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.MatchUtil;
import com.mars.core.util.MesUtil;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 执行拦截器
 * @author yuye
 *
 */
public class ExecuteInters {
	
	private static MarsLogger logger = MarsLogger.getLogger(ExecuteInters.class);

	/**
	 * 执行拦截器的开始方法
	 * @param list jihe
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public static Object executeIntersStart(List<Object> list,HttpRequest request, HttpResponse response) {
		Class<?> clss = null;
		try {
			for(Object obj : list) {
				clss = obj.getClass();
				
				Method method2 = clss.getDeclaredMethod("startRequest", new Class[] { HttpRequest.class, HttpResponse.class });
				Object result = method2.invoke(obj, new Object[] { request, response });
				if(!result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型["+clss.getName()+"]",e);
			return errorResult(clss);
		} 
		
	}
	
	/**
	 * 执行拦截器的结束方法
	 * @param list jihe
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public static Object executeIntersEnd(List<Object> list,HttpRequest request, HttpResponse response,Object conResult) {
		Class<?> clss = null;
		try {
			
			for(Object obj : list) {
				clss = obj.getClass();
				
				Method method2 = clss.getDeclaredMethod("endRequest", new Class[] { HttpRequest.class, HttpResponse.class, Object.class });
				Object result = method2.invoke(obj, new Object[] { request, response, conResult });
				if(!result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型["+clss.getName()+"]",e);
			return errorResult(clss);
		} 
	}
	
	/**
	 * 获取所有符合条件的拦截器
	 * @param uriEnd uel
	 * @return duix
	 */
	public static List<Object> getInters(String uriEnd){
		
		try {
			List<Object> list = new ArrayList<>();

			Object objs = MarsSpace.getEasySpace().getAttr(MarsConstant.INTERCEPTORS);

			if(objs != null) {
				List<Map<String,Object>> interceptors = (List<Map<String,Object>>)objs;

				for(Map<String,Object> map : interceptors) {

					MarsInterceptor marsInterceptor = (MarsInterceptor)map.get("annotation");
					String pattern = marsInterceptor.pattern();

					if(MatchUtil.isMatch(pattern, uriEnd)){
						Class cls = (Class)map.get("className");
						list.add(cls.getDeclaredConstructor().newInstance());
					}
				}
			}


			return list;
		} catch (Exception e) {
			logger.error("读取拦截器报错",e);
			return new ArrayList<>();
		}
	}

	/**
	 * 返回错误信息
	 * @param cls
	 * @return
	 */
	private static JSONObject errorResult(Class<?> cls) {
		return MesUtil.getMes(500,"执行拦截器报错，拦截器类型["+cls.getName()+"]");
	}
}
