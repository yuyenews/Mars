package com.yuyenews.resolve.model;

import com.yuyenews.core.annotation.enums.RequestMetohd;

/**
 * 控制器映射实体
 * 
 * @author yuye
 *
 */
public class EasyMappingModel {

	/**
	 * 对象
	 */
	private Object object;

	/**
	 * 请求方式
	 */
	private RequestMetohd requestMetohd;

	/**
	 * 映射的方法
	 */
	private String method;

	/**
	 * 控制层class对象
	 */
	private Class<?> cls;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public RequestMetohd getRequestMetohd() {
		return requestMetohd;
	}

	public void setRequestMetohd(RequestMetohd requestMetohd) {
		this.requestMetohd = requestMetohd;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

}
