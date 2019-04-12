package com.mars.mvc.base;

import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;

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
	Object startRequest(HttpRequest request,HttpResponse response);
	
	/**
	 * 控制层执行之后
	 * @param request
	 * @param response
	 * @param obj 控制层返回的数据
	 * @return
	 */
	Object endRequest(HttpRequest request,HttpResponse response,Object obj);
}
