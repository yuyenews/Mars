package com.mars.aio.server.request.impl;

import com.mars.common.annotation.enums.ReqMethod;
import com.mars.server.http.request.MartianHttpExchange;
import com.mars.aio.server.request.HttpMarsRequest;

/**
 * 请求对象
 * @author yuye
 *
 */
public class HttpMarsDefaultRequest extends HttpMarsRequest {

	/**
	 * java原生request
	 */
	private MartianHttpExchange httpExchange;

	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpExchange
	 */
	public HttpMarsDefaultRequest(MartianHttpExchange httpExchange) {
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
			if(getMethod().toUpperCase().equals(ReqMethod.GET.toString())){
				return "N";
			}
			String contentType = httpExchange.getContentType();
			if(contentType == null){
				return "N";
			}
			return contentType;
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
		return httpExchange.getRequestHeaders().get(key);
	}

	@Override
	public String getInetSocketAddress() {
		return httpExchange.getRequestHeaders().get("Host");
	}
}
