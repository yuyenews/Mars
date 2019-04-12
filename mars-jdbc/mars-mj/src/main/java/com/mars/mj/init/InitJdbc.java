package com.mars.mj.init;

import com.alibaba.druid.pool.DruidDataSource;
import com.mars.core.constant.EasySpace;
import com.mars.mj.helper.DBHelper;
import com.mars.mj.proxy.MjProxy;
import com.mars.jdbc.base.BaseInitJdbc;
import com.mars.jdbc.load.LoadDaos;

import java.util.Map;

/**
 * 初始化jdbc
 * @author yuye
 *
 */
public class InitJdbc implements BaseInitJdbc {

	private static EasySpace easySpace = EasySpace.getEasySpace();

	/**
	 * 加载配置
	 */
	@Override
	public void init() throws Exception{

		/* 加载mj数据源 */
		loadDataSource();

		/* 创建dao对象 */
		LoadDaos.loadDao(new MjProxy());
		
	}

	/**
	 * 加载数据源
	 * @throws Exception
	 */
	private void loadDataSource() throws Exception {
		Map<String,DruidDataSource> druidDataSourceMap = DBHelper.getDruidDataSources();
		easySpace.setAttr("druidDataSourceMap",druidDataSourceMap);

		/* 保存默认数据源名称 */
		easySpace.setAttr("defaultDataSource", DBHelper.getDefaultDataSourceName());
	}
}
