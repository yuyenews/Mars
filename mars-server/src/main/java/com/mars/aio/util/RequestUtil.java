package com.mars.aio.util;

import com.mars.aio.server.request.HttpMarsRequest;

/**
 * 请求工具类
 * @author yuye
 *
 */
public class RequestUtil {

	/**
	 * 从uri中提取最末端
	 * @param request 请求
	 * @return string
	 */
	public static String getUriName(HttpMarsRequest request) {
		/* 获取路径 */
		String uri = request.getUrl();
		if(uri.startsWith("/")){
			return subUri(uri, 0);
		} else {
			int start = uri.lastIndexOf("/");
			return subUri(uri, start);
		}
	}

	/**
	 * 截取请求路径
	 * @param uri
	 * @param start
	 * @return
	 */
	private static String subUri(String uri, int start){
		int end = uri.indexOf("?");
		if(end > -1){
			return uri.substring(start, end);
		} else {
			return uri.substring(start);
		}
	}
}
