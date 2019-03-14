package com.yuyenews.easy.server.request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局对象，类似于tomcat的servletcontext
 * 
 * @author yuye
 *
 */
public class HttpContext {

	private static HttpContext context;

	private Map<String, Object> map = new ConcurrentHashMap<>();

	private HttpContext() {
	}

	public static HttpContext getHttpContext() {
		if (context == null) {
			context = new HttpContext();
		}

		return context;
	}

	/**
	 * 往context里添加数据
	 * 
	 * @param key 键
	 * @param value 值
	 */
	public void setAttr(String key, Object value) {
		map.put(key, value);
	}

	/**
	 * 从context里获取数据
	 * 
	 * @param key 键
	 * @return 值
	 */
	public Object getAttr(String key) {
		return map.get(key);
	}
}
