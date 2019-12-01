package com.mars.server.util;

import com.mars.server.server.request.HttpMarsRequest;

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
		if(uri.indexOf("?")>-1) {
			uri = uri.substring(uri.lastIndexOf("/"),uri.indexOf("?"));
		} else {
			uri = uri.substring(uri.lastIndexOf("/"));
		}
		return uri;
	}
}
