package com.mars.start;

import com.mars.mybatis.init.InitJdbc;
import com.mars.start.base.BaseStartMars;

/**
 * 启动Mars框架
 * @author yuye
 *
 */
public class StartMars {
	/**
	 * 启动Mars框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz) {
		BaseStartMars.start(clazz,new InitJdbc());
	}
}
