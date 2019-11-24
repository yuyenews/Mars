package com.mars.mvc.model;

import com.mars.core.annotation.enums.ReqMethod;

/**
 * 控制器映射实体
 * 
 * @author yuye
 *
 */
public class MarsMappingModel {

	/**
	 * 对象
	 */
	private Object object;

	/**
	 * 请求方式
	 */
	private ReqMethod reqMethod;

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

	public ReqMethod getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(ReqMethod reqMethod) {
		this.reqMethod = reqMethod;
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
