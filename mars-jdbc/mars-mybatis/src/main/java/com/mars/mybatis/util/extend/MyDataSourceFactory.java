package com.mars.mybatis.util.extend;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mars.core.logger.MarsLogger;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class MyDataSourceFactory extends DruidDataSourceFactory implements DataSourceFactory {
	
	private MarsLogger logger = MarsLogger.getLogger(MyDataSourceFactory.class);

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
