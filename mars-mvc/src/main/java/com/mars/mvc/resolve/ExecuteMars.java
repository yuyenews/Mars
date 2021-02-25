package com.mars.mvc.resolve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.common.annotation.enums.ReqMethod;
import com.mars.common.constant.MarsConstant;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.mvc.load.model.MarsInterModel;
import com.mars.mvc.util.ParamsCheckUtil;
import com.mars.iserver.par.factory.ParamAndResultFactory;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.util.RequestUtil;
import com.mars.mvc.load.model.MarsMappingModel;
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
			String isSuccess = checkRequestMethod(method, marsMappingModel);
			if(isSuccess != null){
				return isSuccess;
			}

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
		Method method = marsMappingModel.getExeMethod();

		/* 获取前端传参 */
		Object[] params = ParamAndResultFactory.getBaseParamAndResult().getParam(method,request,response);

		/* 校验传参 */
		JSONObject checkResult = ParamsCheckUtil.checkParam(params,method);
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
	private String checkRequestMethod(String method,MarsMappingModel marsMappingModel) throws Exception {
		if(marsMappingModel == null){
			throw new Exception("服务器上没有相应的接口");
		}

		String strMethod = method.toLowerCase();

		/* 如果请求方式是options，那就说明这是一个试探性的请求，直接响应即可 */
		if(strMethod.equals(MarsConstant.OPTIONS.toLowerCase())){
			return MarsConstant.OPTIONS;
		}

		/* 校验请求方法 是否一致 */
		ReqMethod[] reqMethods = marsMappingModel.getReqMethod();
		for(ReqMethod reqMethod : reqMethods){
			if (strMethod.equals(reqMethod.name().toLowerCase())) {
				return null;
			}
		}

		/* 如果请求方式和MarsApi的映射不一致，则提示客户端 */
		throw new Exception("此接口支持的请求方式为[" + JSON.toJSONString(reqMethods) + "]");
	}
}
