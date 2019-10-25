package com.mars.server.server.request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局对象，类似于tomcat的servletContext
 * 
 * @author yuye
 *
 */
public class HttpMarsContext {

	private static HttpMarsContext context;

	private Map<String, Object> map = new ConcurrentHashMap<>();

	private HttpMarsContext() {
	}

	public static HttpMarsContext getHttpContext() {
		if (context == null) {
			context = new HttpMarsContext();
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

	/**
	 * 删除数据
	 * @param key 键
	 */
	public void remove(String key){
		map.remove(key);
	}
}
