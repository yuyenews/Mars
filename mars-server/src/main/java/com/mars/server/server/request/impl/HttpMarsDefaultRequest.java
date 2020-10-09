package com.mars.server.server.request.impl;

import com.mars.server.server.request.HttpMarsRequest;
import com.sun.net.httpserver.HttpExchange;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 请求对象
 * @author yuye
 *
 */
public class HttpMarsDefaultRequest extends HttpMarsRequest {

	/**
	 * java原生request
	 */
	private HttpExchange httpExchange;

	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpExchange
	 */
	public HttpMarsDefaultRequest(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	/**
	 * 获取java原生request
	 * @return 原生请求对象
	 */
	public <T> T getNativeRequest(Class<T> cls) {
		return (T)httpExchange;
	}

	/**
	 * 获取参数类型
	 * @return 参数类型
	 */
	public String getContentType(){
		try {
			if(getMethod().toUpperCase().equals("GET")){
				return "N";
			}
			List<String> ctList = httpExchange.getRequestHeaders().get("Content-type");
			if(ctList == null || ctList.size() < 1){
				return "N";
			}
			return ctList.get(0).trim().toLowerCase();
		} catch (Exception e){
			return "N";
		}
	}

	/**
	 * 获取请求方法
	 * @return 请求方法
	 */
	public String getMethod() {
		return httpExchange.getRequestMethod();
	}

	/**
	 * 获取要请求的uri
	 * @return 请求方法
	 */
	public String getUrl() {
		return httpExchange.getRequestURI().toString();
	}
	
	/**
	 * 获取请求头数据
	 * @param key 键
	 * @return 头数据
	 */
	public String getHeader(String key) {
		List<String> headers = getHeaders(key);
		if(headers == null || headers.size() < 1){
			return null;
		}
		return headers.get(0);
	}

	/**
	 * 获取请求头数据
	 * @param key 键
	 * @return 头数据
	 */
	public List<String> getHeaders(String key) {
		return httpExchange.getRequestHeaders().get(key);
	}

	/**
	 * 获取客户端InetSocketAddress
	 * @return inetSocketAddress
	 */
	public String getInetSocketAddress() {
		InetSocketAddress inetSocketAddress = httpExchange.getLocalAddress();
		StringBuffer result = new StringBuffer();
		result.append(inetSocketAddress.getHostString());
		result.append(":");
		result.append(inetSocketAddress.getPort());

        return result.toString();
	}
}
