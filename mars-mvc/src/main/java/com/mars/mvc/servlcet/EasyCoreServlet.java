package com.mars.mvc.servlcet;

import com.mars.mvc.resolve.ResolveRequest;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.MesUtil;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import com.mars.server.server.servlet.EasyServlet;

/**
 * 核心servlet，用于接收所有请求，并调用相应的方法进行处理
 * @author yuye
 *
 */
public class EasyCoreServlet implements EasyServlet{
	
	private static MarsLogger log = MarsLogger.getLogger(EasyCoreServlet.class);
	
	@Override
	public Object doRequest(HttpRequest request, HttpResponse response) {
		try {

			/* 将请求丢给解释器 去解释，并调用对应的控制层方法进行处理 */
			ResolveRequest resolveRequest = ResolveRequest.getResolveRequest();
			Object result = resolveRequest.resolve(request,response);

			/*将控制层 返回的结果 返回给netty，让其响应给客户端*/
			return result;

		} catch (Exception e) {
			log.error("解释请求的时候报错",e);
		}
		return MesUtil.getMes(500,"解析请求报错");
	}

}
