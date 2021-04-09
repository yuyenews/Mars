package com.mars.mvc.base;

import com.mars.aio.server.request.HttpMarsRequest;
import com.mars.aio.server.request.HttpMarsResponse;

import java.util.List;

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
	 * 控制层执行之前
	 * @param request
	 * @param response
	 * @return
	 */
	Object beforeRequest(HttpMarsRequest request, HttpMarsResponse response);
	
	/**
	 * 控制层执行之后
	 * @param request
	 * @param response
	 * @param obj 控制层返回的数据
	 * @return
	 */
	Object afterRequest(HttpMarsRequest request, HttpMarsResponse response, Object obj);

	/**
	 * 不拦截的接口
	 * @return
	 */
	List<String> exclude();
}
