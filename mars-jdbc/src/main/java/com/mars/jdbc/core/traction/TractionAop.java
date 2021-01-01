package com.mars.jdbc.core.traction;

import com.alibaba.druid.pool.DruidDataSource;
import com.mars.aop.base.BaseTraction;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.aop.model.AopModel;
import com.mars.common.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务管理aop
 * @author yuye
 *
 */
public class TractionAop implements BaseTraction {

	private Logger logger = LoggerFactory.getLogger(TractionAop.class);

	private static MarsSpace marsSpace = MarsSpace.getEasySpace();

	/**
	 * 数据源集合
	 */
	private static Map<String, DruidDataSource> maps;
	
	/**
	 * 获取数据库连接，并设置为不自动提交
	 * 
	 * 将获取到的连接 放到缓存中
	 * 
	 * @param aopModel aop对象
	 */
	@Override
	public void beginTraction(AopModel aopModel) {
		try {
			if(maps == null){
				/* 这里不怕线程安全问题，因为无论进来多少次都是一样的值 */
				maps = (Map<String,DruidDataSource>) marsSpace.getAttr(MarsConstant.DATA_SOURCE_MAP);
			}

			Map<String,Connection> connections = new HashMap<>();

			for(String key : maps.keySet()) {
				Connection connection = maps.get(key).getConnection();
				connection.setAutoCommit(false);
				connection.setTransactionIsolation(aopModel.getTractionLevel().getLevel());
				connections.put(key, connection);
			}

			ThreadUtil.getThreadLocal().set(connections);
		} catch (Exception e) {
			logger.error("开启事务出错",e);
		}
	}

	/**
	 * 从缓存中获取当前线程的数据库连接，并提交事务
	 *
	 */
	@Override
	public void commit() {
		try {
			Map<String,Connection> connections = (Map<String,Connection>) ThreadUtil.getThreadLocal().get();

			for(String key : connections.keySet()) {
				Connection connection = connections.get(key);
				connection.commit();
				connection.close();
			}
		} catch (Exception e) {
			logger.error("提交事务出错",e);
		} finally {
			ThreadUtil.getThreadLocal().remove();
		}
	}

	/**
	 * 从缓存中获取当前线程的数据库连接，并回滚事务
	 * @param e 异常
	 */
	@Override
	public void rollback(Throwable e) {
		try {
			Map<String,Connection> connections = (Map<String,Connection>) ThreadUtil.getThreadLocal().get();

			for(String key : connections.keySet()) {
				Connection connection = connections.get(key);
				connection.rollback();
				connection.close();
			}

			logger.error("",e);
		} catch (Exception ex) {
			logger.error("回滚事务出错",ex);
		} finally {
			ThreadUtil.getThreadLocal().remove();
		}
	}
}
