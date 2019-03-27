package com.yuyenews.mybatis.init;

import com.yuyenews.mybatis.proxy.MappersProxy;
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

		/* 加载mybatis配置 */
		LoadSqlSessionFactory.getLoadSqlSessionFactory();
		
		/* 创建dao对象 */
		LoadDaos.loadDao(new MappersProxy());
		
	}
}
