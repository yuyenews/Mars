package com.yuyenews.aop.base;

/**
 * AOP定义模板
 * @author yuye
 *
 */
public interface BaseAop {

	/**
	 * 方法开始前调用
	 * @param args 参数
	 */
	void startMethod(Object[] args);
	
	/**
	 * 方法结束后调用
	 * @param args 参数
	 */
	void endMethod(Object[] args);
	
	/**
	 * 出异常后调用
	 */
	void exp(Throwable e);
}
