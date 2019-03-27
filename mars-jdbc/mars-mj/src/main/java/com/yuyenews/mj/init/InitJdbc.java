package com.yuyenews.mj.init;

import com.yuyenews.mj.proxy.MjProxy;
import com.yuyenews.jdbc.base.BaseInitJdbc;
import com.yuyenews.jdbc.load.LoadDaos;

/**
 * 初始化jdbc
 * @author yuye
 *
 */
public class InitJdbc implements BaseInitJdbc {

	/**
	 * 加载配置
	 */
	@Override
	public void init() throws Exception{

		/* 加载mj配置 */

		/* 创建dao对象 */
		LoadDaos.loadDao(new MjProxy());
		
	}
}
