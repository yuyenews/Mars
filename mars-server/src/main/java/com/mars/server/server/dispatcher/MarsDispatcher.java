package com.mars.server.server.dispatcher;

import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;

/**
 * servlet 模板
 * @author yuye
 *
 */
public interface MarsDispatcher {


	/**
	 * 请求接受方法
	 * @param request
	 * @param response
	 * @return obj
	 */
	Object doRequest(HttpMarsRequest request, HttpMarsResponse response) throws Exception;
}
