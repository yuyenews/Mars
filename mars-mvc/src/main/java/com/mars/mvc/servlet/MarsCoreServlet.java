package com.mars.mvc.servlet;

import com.alibaba.fastjson.JSON;
import com.mars.core.enums.DataType;
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
			Object result = resolveRequest.resolve(request,response);

			/*将控制层 返回的结果 返回给netty，让其响应给客户端*/
			if(isNotObject(result)) {
				return result;
			} else {
				return JSON.toJSONString(result);
			}
		} catch (Exception e) {
			log.error("解释请求的时候报错",e);
			throw e;
		}
	}

	/**
	 * 判断是否是对象
	 * @param result
	 * @return
	 */
	public boolean isNotObject(Object result){
		if(result == null){
			return true;
		}
		String fieldTypeName = result.getClass().getSimpleName().toUpperCase();
		switch (fieldTypeName){
			case DataType.INT:
			case DataType.INTEGER:
			case DataType.BYTE:
			case DataType.STRING:
			case DataType.CHAR:
			case DataType.CHARACTER:
			case DataType.DOUBLE:
			case DataType.FLOAT:
			case DataType.LONG:
			case DataType.SHORT:
			case DataType.BOOLEAN:
				return true;
		}
		return false;
	}
}
