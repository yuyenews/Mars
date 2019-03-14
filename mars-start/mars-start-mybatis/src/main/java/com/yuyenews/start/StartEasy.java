package com.yuyenews.start;

import com.yuyenews.easy.init.InitJdbc;
import com.yuyenews.start.base.BaseStartEasy;

/**
 * 启动easy框架
 * @author yuye
 *
 */
public class StartEasy {
	


	/**
	 * 启动easy框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz) {
		BaseStartEasy.start(clazz,new InitJdbc());
	}
	
}
