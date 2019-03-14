package com.yuyenews.easy.util.extend;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.yuyenews.core.logger.GogeLogger;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class MyDataSourceFactory extends DruidDataSourceFactory implements DataSourceFactory {
	
	private GogeLogger logger = GogeLogger.getLogger(MyDataSourceFactory.class);

	protected Properties properties;

	@Override
	public void setProperties(Properties props) {
		this.properties = props;
	}

	@Override
	public DataSource getDataSource() {
		try {
			return createDataSource(properties);
		} catch (Exception e) {
			logger.error("",e);
		}

		return null;
	}

}
