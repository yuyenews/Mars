package com.yuyenews.easy.server.servlet;

import com.yuyenews.easy.server.request.HttpRequest;
import com.yuyenews.easy.server.request.HttpResponse;

/**
 * servlet 模板
 * @author yuye
 *
 */
public interface EasyServlet {


	/**
	 * 请求接受方法
	 * @param request
	 * @param response
	 * @return obj
	 */
	Object doRequest(HttpRequest request,HttpResponse response);
}
