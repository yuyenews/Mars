package com.mars.mvc.load.model;

import com.mars.common.annotation.enums.ReqMethod;

import java.lang.reflect.Method;

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
	 * 映射的方法名称
	 */
	private String method;

	/**
	 * 映射的方法
	 */
	private Method exeMethod;

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

	public Method getExeMethod() {
		return exeMethod;
	}

	public void setExeMethod(Method exeMethod) {
		this.exeMethod = exeMethod;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}
}
