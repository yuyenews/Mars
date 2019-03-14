package com.yuyenews.servlcet;

import com.yuyenews.core.logger.GogeLogger;
import com.yuyenews.core.util.MesUtil;
import com.yuyenews.easy.server.request.HttpRequest;
import com.yuyenews.easy.server.request.HttpResponse;
import com.yuyenews.easy.server.servlet.EasyServlet;
import com.yuyenews.resolve.ResolveRequest;

/**
 * 核心servlet，用于接收所有请求，并调用相应的方法进行处理
 * @author yuye
 *
 */
public class EasyCoreServlet implements EasyServlet{
	
	private static GogeLogger log = GogeLogger.getLogger(EasyCoreServlet.class);
	
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
