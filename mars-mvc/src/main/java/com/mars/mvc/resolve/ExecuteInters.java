package com.mars.mvc.resolve;

import com.alibaba.fastjson.JSONObject;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.MatchUtil;
import com.mars.common.util.MesUtil;
import com.mars.mvc.constant.InterConstant;
import com.mars.mvc.model.MarsInterModel;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行拦截器
 * @author yuye
 *
 */
public class ExecuteInters {
	
	private static Logger logger = LoggerFactory.getLogger(ExecuteInters.class);

	/**
	 * 执行拦截器的开始方法
	 * @param list 拦截器集合
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return 拦截器的返回数据
	 */
	public static Object executeIntersStart(List<MarsInterModel> list, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
		String className = "";
		try {
			for(MarsInterModel marsInterModel : list) {
				className = marsInterModel.getCls().getName();

				Method method2 = marsInterModel.getCls().getDeclaredMethod(InterConstant.BEFORE_REQUEST, new Class[] { HttpMarsRequest.class, HttpMarsResponse.class });
				Object result = method2.invoke(marsInterModel.getObj(), new Object[] { request, response });
				if(result == null || !result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型["+className+"]",e);
			throw new Exception(errorResult(className,e),e) ;
		}
	}
	
	/**
	 * 执行拦截器的结束方法
	 * @param list 拦截器集合
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param conResult api的返回数据
	 * @return 拦截器的返回数据
	 */
	public static Object executeIntersEnd(List<MarsInterModel> list, HttpMarsRequest request, HttpMarsResponse response, Object conResult) throws Exception {
		String className = "";
		try {
			
			for(MarsInterModel marsInterModel : list) {
				className = marsInterModel.getCls().getName();

				Method method2 = marsInterModel.getCls().getDeclaredMethod(InterConstant.AFTER_REQUEST, new Class[] { HttpMarsRequest.class, HttpMarsResponse.class, Object.class });
				Object result = method2.invoke(marsInterModel.getObj(), new Object[] { request, response, conResult });
				if(result == null || !result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型["+className+"]",e);
			throw new Exception(errorResult(className,e),e) ;
		} 
	}
	
	/**
	 * 获取所有符合条件的拦截器
	 * @param uriEnd url
	 * @return 拦截器集合
	 */
	public static List<MarsInterModel> getInters(String uriEnd) {
		try {
			List<MarsInterModel> list = new ArrayList<>();

			Object interceptorsObj = MarsSpace.getEasySpace().getAttr(MarsConstant.INTERCEPTOR_OBJECTS);
			if(interceptorsObj == null) {
				return list;
			}

			List<MarsInterModel> interceptors = (List<MarsInterModel>)interceptorsObj;
			for(MarsInterModel marsInterModel : interceptors) {
				if(MatchUtil.isMatch(marsInterModel.getPattern(), uriEnd.toUpperCase())){
					if(hasExclude(marsInterModel,uriEnd)){
						/* 如果此url在此拦截器的排除名单中，则不进行拦截 */
						continue;
					}
					list.add(marsInterModel);
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("读取拦截器报错",e);
			return new ArrayList<>();
		}
	}

	/**
	 * 判断拦截器是否需要拦截此接口
	 * @param marsInterModel
	 * @return
	 * @throws Exception
	 */
	private static Boolean hasExclude(MarsInterModel marsInterModel,String uriEnd) throws Exception {
		Method method2 = marsInterModel.getCls().getDeclaredMethod(InterConstant.EXCLUDE);
		Object result = method2.invoke(marsInterModel.getObj());
		if(result == null) {
			return false;
		}
		List<String> excludeList = (List<String>)result;
		Boolean hasExc = excludeList.contains(uriEnd);
		return hasExc;
	}

	/**
	 * 返回错误信息
	 * @param className 拦截器类名
	 * @return
	 */
	private static String errorResult(String className,Exception e) {
		JSONObject jsonObject = MesUtil.getMes(500,"执行拦截器报错，拦截器类型["+className+"],"+e.getMessage());
		return jsonObject.toJSONString();
	}
}
