package com.mars.jdbc.core.load;

import com.alibaba.druid.pool.DruidDataSource;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.ncfg.traction.TractionFactory;
import com.mars.jdbc.core.helper.base.DBHelper;
import com.mars.jdbc.core.traction.TractionAop;

import java.util.Map;

/**
 * 初始化jdbc
 * @author yuye
 *
 */
public class InitJdbc {

	private static MarsSpace marsSpace = MarsSpace.getEasySpace();

	/**
	 * 加载配置
	 */
	public void init() throws Exception{

		/* 设置处理事务的类 */
		TractionFactory.setTraction(new TractionAop());

		/* 加载数据源 */
		loadDataSource();

		/* 创建dao对象 */
		LoadDaos.loadDao();
		
	}

	/**
	 * 加载数据源
	 * @throws Exception
	 */
	private void loadDataSource() throws Exception {
		Map<String,DruidDataSource> druidDataSourceMap = DBHelper.getDruidDataSources();
		marsSpace.setAttr(MarsConstant.DATA_SOURCE_MAP,druidDataSourceMap);

		/* 保存默认数据源名称 */
		marsSpace.setAttr(MarsConstant.DEFAULT_DATASOURCE_NAME, DBHelper.getDefaultDataSourceName());
	}
}
