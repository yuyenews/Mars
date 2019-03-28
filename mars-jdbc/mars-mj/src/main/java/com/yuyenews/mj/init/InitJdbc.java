package com.yuyenews.mj.init;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.mj.helper.DBHelper;
import com.yuyenews.mj.proxy.MjProxy;
import com.yuyenews.jdbc.base.BaseInitJdbc;
import com.yuyenews.jdbc.load.LoadDaos;

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

		int i = 0;

		for(String defaultDataSource : druidDataSourceMap.keySet()){
			if(i == 0){
				easySpace.setAttr("defaultDataSource", defaultDataSource);
			}
		}
	}
}
