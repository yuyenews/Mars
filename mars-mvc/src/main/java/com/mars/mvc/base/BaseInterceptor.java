package com.mars.mvc.base;

import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;

/**
 * 拦截器基类，强制继承
 * @author yuye
 *
 */
public interface BaseInterceptor {
	
	/**
	 * 通过
	 */
	String SUCCESS = "success";
	
	/**
	 * 不通过
	 */
	String ERROR = "error";

	/**
	 * 控制层执行之前
	 * @param request
	 * @param response
	 * @return
	 */
	Object startRequest(HttpMarsRequest request, HttpMarsResponse response);
	
	/**
	 * 控制层执行之后
	 * @param request
	 * @param response
	 * @param obj 控制层返回的数据
	 * @return
	 */
	Object endRequest(HttpMarsRequest request, HttpMarsResponse response, Object obj);
}
