package com.mars.mvc.resolve;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.constant.MarsConstant;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.mvc.model.MarsInterModel;
import com.mars.mvc.util.ParamsCheckUtil;
import com.mars.netty.par.factory.ParamAndResultFactory;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.util.RequestUtil;
import com.mars.mvc.model.MarsMappingModel;
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
public class ExecuteMars {

	private Logger log = LoggerFactory.getLogger(ExecuteMars.class);

	private static ExecuteMars executeMars = new ExecuteMars();

	private ExecuteMars() {
	}

	public static ExecuteMars getExecuteMars() {
		return executeMars;
	}

	/**
	 * 执行MarsApi
	 * @param marsMappingModel duix
	 * @param method fangfa
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public Object execute(MarsMappingModel marsMappingModel, String method, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
		try {

			/* 校验请求方式 */
			checkRequestMethod(method, marsMappingModel);

			/* 获取拦截器 */
			String uriEnd = RequestUtil.getUriName(request);
			List<MarsInterModel> list = ExecuteInters.getInters(uriEnd);

			/* 执行拦截器 在控制层执行前的方法 */
			Object intersStartResult = ExecuteInters.executeIntersStart(list,request, response);
			if(!intersStartResult.toString().equals(BaseInterceptor.SUCCESS)) {
				return intersStartResult;
			}

			/* 执行MarsApi的方法 */
			Object result = executeMarsApiMethod(marsMappingModel,request,response);

			/* 执行拦截器 在控制层执行后的方法 */
			Object intersEndResult = ExecuteInters.executeIntersEnd(list,request, response,result);
			if(!intersEndResult.toString().equals(BaseInterceptor.SUCCESS)) {
				return intersEndResult;
			}
			return result;
		} catch (Exception e) {
			log.error("执行控制层的时候报错",e);
			throw e;
		}
	}

	/**
	 * 执行MarsApi的方法
	 * @param marsMappingModel
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private Object executeMarsApiMethod(MarsMappingModel marsMappingModel, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
		Object obj = marsMappingModel.getObject();
		Method method = getMethod(marsMappingModel);

		/* 获取前端传参 */
		Object[] params = ParamAndResultFactory.getBaseParamAndResult().getParam(method,request,response);

		/* 校验传参 */
		JSONObject checkResult = ParamsCheckUtil.checkParam(params);
		if(checkResult != null){
			return checkResult;
		}

		/* 执行api方法 */
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
		Method[] methods = cls.getMethods();
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

	/**
	 * 校验请求方式和接口设置的方式是否一致
	 * @param method
	 * @param marsMappingModel
	 * @throws Exception
	 */
	private void checkRequestMethod(String method,MarsMappingModel marsMappingModel) throws Exception {
		if(marsMappingModel == null){
			throw new Exception("服务器上没有相应的接口");
		}

		String strMethod = method.toLowerCase();
		String requestMethod = marsMappingModel.getReqMethod().name().toLowerCase();
		if (!strMethod.equals(requestMethod)) {
			/* 如果请求方式和MarsApi的映射不一致，则提示客户端 */
			throw new Exception("此接口的请求方式为[" + requestMethod + "]");
		}
	}
}
