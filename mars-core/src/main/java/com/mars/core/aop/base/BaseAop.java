package com.mars.core.aop.base;

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
	 * @param result 被监听方法的返回值
	 */
	void endMethod(Object[] args, Object result);
	
	/**
	 * 出异常后调用
	 */
	void exp(Throwable e);
}
