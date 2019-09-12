package com.mars.mybatis.util.extend;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class MyDataSourceFactory extends DruidDataSourceFactory implements DataSourceFactory {
	
	private Logger logger = LoggerFactory.getLogger(MyDataSourceFactory.class);

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
