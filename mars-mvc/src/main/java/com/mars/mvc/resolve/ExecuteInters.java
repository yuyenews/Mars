package com.mars.mvc.resolve;

import com.alibaba.fastjson.JSONObject;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.MatchUtil;
import com.mars.core.util.MesUtil;
import com.mars.mvc.model.MarsInterModel;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	public static Object executeIntersStart(List<MarsInterModel> list,HttpRequest request, HttpResponse response) throws Exception {
		Class<?> clss = null;
		try {
			for(MarsInterModel marsInterModel : list) {
				clss = marsInterModel.getCls();
				
				Method method2 = clss.getDeclaredMethod("startRequest", new Class[] { HttpRequest.class, HttpResponse.class });
				Object result = method2.invoke(marsInterModel.getObj(), new Object[] { request, response });
				if(!result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型["+clss.getName()+"]",e);
			throw new Exception(errorResult(clss,e),e) ;
		}
	}
	
	/**
	 * 执行拦截器的结束方法
	 * @param list jihe
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public static Object executeIntersEnd(List<MarsInterModel> list,HttpRequest request, HttpResponse response,Object conResult) throws Exception {
		Class<?> clss = null;
		try {
			
			for(MarsInterModel marsInterModel : list) {
				clss = marsInterModel.getCls();
				
				Method method2 = clss.getDeclaredMethod("endRequest", new Class[] { HttpRequest.class, HttpResponse.class, Object.class });
				Object result = method2.invoke(marsInterModel.getObj(), new Object[] { request, response, conResult });
				if(!result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型["+clss.getName()+"]",e);
			throw new Exception(errorResult(clss,e),e) ;
		} 
	}
	
	/**
	 * 获取所有符合条件的拦截器
	 * @param uriEnd uel
	 * @return duix
	 */
	public static List<MarsInterModel> getInters(String uriEnd){
		try {
			List<MarsInterModel> list = new ArrayList<>();

			Object interceptorsObj = MarsSpace.getEasySpace().getAttr(MarsConstant.INTERCEPTOR_OBJECTS);

			if(interceptorsObj != null) {
				List<MarsInterModel> interceptors = (List<MarsInterModel>)interceptorsObj;
				for(MarsInterModel marsInterModel : interceptors) {
					if(MatchUtil.isMatch(marsInterModel.getPattern(), uriEnd)){
						list.add(marsInterModel);
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
	private static String errorResult(Class<?> cls,Exception e) {
		JSONObject jsonObject = MesUtil.getMes(500,"执行拦截器报错，拦截器类型["+cls.getName()+"],"+e.getMessage());
		return jsonObject.toJSONString();
	}
}
