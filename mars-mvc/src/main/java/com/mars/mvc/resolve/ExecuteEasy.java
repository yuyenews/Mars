package com.mars.mvc.resolve;

import com.mars.core.constant.MarsConstant;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.mvc.model.MarsInterModel;
import com.mars.mvc.util.BuildParams;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import com.mars.server.util.RequestUtil;
import com.mars.mvc.model.MarsMappingModel;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 执行器
 * 
 * @author yuye
 *
 */
public class ExecuteEasy {

	private Logger log = LoggerFactory.getLogger(ExecuteEasy.class);

	private static ExecuteEasy executeEasy;

	private ExecuteEasy() {
	}

	public static ExecuteEasy getExecuteEasy() {
		if (executeEasy == null) {
			executeEasy = new ExecuteEasy();
		}
		return executeEasy;
	}

	/**
	 * 执行controller
	 * @param marsMappingModel duix
	 * @param method fangfa
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public Object execute(MarsMappingModel marsMappingModel, HttpMethod method, HttpRequest request, HttpResponse response) throws Exception {
		try {
			
			if(marsMappingModel == null) {
				throw new Exception("服务器上没有相应的接口");
			}
			
			String strMethod = method.name().toLowerCase();

			String requestMethod = marsMappingModel.getRequestMetohd().name().toLowerCase();
			
			if (strMethod.equals(requestMethod)) {

				/* 获取拦截器 并执行 控制层执行前的方法 */
				String uriEnd = RequestUtil.getUriName(request);
				List<MarsInterModel> list = ExecuteInters.getInters(uriEnd);
				Object intersStartResult = ExecuteInters.executeIntersStart(list,request, response);
				if(!intersStartResult.toString().equals(BaseInterceptor.SUCCESS)) {
					return intersStartResult;
				}

				/* 执行controller的方法 */
				Object result = executeControllerMethod(marsMappingModel,request,response);

				/* 执行拦截器 在控制层执行后的方法 */
				Object intersEndResult = ExecuteInters.executeIntersEnd(list,request, response,result);
				if(!intersEndResult.toString().equals(BaseInterceptor.SUCCESS)) {
					return intersEndResult;
				}
				
				return result;
			} else {
				/* 如果请求方式和controller的映射不一致，则提示客户端 */
				throw new Exception("此接口的请求方式为[" + requestMethod + "]");
			}
		} catch (Exception e) {
			log.error("执行控制层的时候报错",e);
			throw e;
		}
	}

	/**
	 * 执行controller的方法
	 * @param marsMappingModel
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private Object executeControllerMethod(MarsMappingModel marsMappingModel, HttpRequest request,HttpResponse response) throws Exception {
		Object obj = marsMappingModel.getObject();
		Method method = getMethod(marsMappingModel);

		Object[] params = BuildParams.builder(method,request,response);

		Object result = null;

		if(params != null){
			result = method.invoke(obj, params);
		} else {
			result = method.invoke(obj);
		}
		if(isVoid(method)){
			result = MarsConstant.VOID;
		}

		return result;
	}

	/**
	 * 根据方法名获取到要执行的方法
	 * 这里的复杂度为O(n)，是为了给方法的参数注入
	 * 如果直接通过name去get，必须事先指定参数类型
	 *
	 * @param marsMappingModel
	 * @return
	 */
	private Method getMethod(MarsMappingModel marsMappingModel){
		Class<?> cls = marsMappingModel.getCls();
		Method[] methods = cls.getDeclaredMethods();
		for(Method methodItem : methods){
			if(methodItem.getName().equals(marsMappingModel.getMethod())){
				return methodItem;
			}
		}
		return null;
	}

	/**
	 * 返回值是否是void
	 * @param method
	 * @return
	 */
	private boolean isVoid(Method method){
		Class cl = method.getReturnType();
		String st = cl.getName();
		return st.toLowerCase().trim().equals("void");
	}
}
