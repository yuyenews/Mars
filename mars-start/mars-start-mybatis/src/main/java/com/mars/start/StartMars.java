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
	public static void start(Class<?> clazz,String[] args) {
		if(args != null && args[0] != null){
			BaseStartMars.start(clazz,new InitJdbc(),args[0],null);
		} else {
			BaseStartMars.start(clazz,new InitJdbc(),null,null);
		}
	}

	/**
	 * 启动Mars框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz) {
		start(clazz,null);
	}
}
