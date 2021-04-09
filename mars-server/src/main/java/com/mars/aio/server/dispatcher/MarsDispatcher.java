package com.mars.aio.server.dispatcher;

import com.mars.aio.server.request.HttpMarsRequest;
import com.mars.aio.server.request.HttpMarsResponse;

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
