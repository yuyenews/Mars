package com.mars.mvc.servlet;

import com.mars.mvc.resolve.ResolveRequest;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.servlet.MarsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心servlet，用于接收所有请求，并调用相应的方法进行处理
 * @author yuye
 *
 */
public class MarsCoreServlet implements MarsServlet {
	
	private Logger log = LoggerFactory.getLogger(MarsCoreServlet.class);
	
	@Override
	public Object doRequest(HttpMarsRequest request, HttpMarsResponse response) throws Exception {
		try {

			/* 将请求丢给解释器 去解释，并调用对应的控制层方法进行处理 */
			ResolveRequest resolveRequest = ResolveRequest.getResolveRequest();
			return resolveRequest.resolve(request,response);
		} catch (Exception e) {
			log.error("解释请求的时候报错",e);
			throw e;
		}
	}
}
