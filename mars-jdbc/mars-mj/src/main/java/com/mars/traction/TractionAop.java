package com.mars.traction;

import com.alibaba.druid.pool.DruidDataSource;
import com.mars.aop.base.BaseAop;
import com.mars.core.constant.EasyConstant;
import com.mars.core.constant.EasySpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.ThreadUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务管理aop
 * @author yuye
 *
 */
public class TractionAop implements BaseAop {

	private MarsLogger logger = MarsLogger.getLogger(TractionAop.class);

	private static EasySpace easySpace = EasySpace.getEasySpace();
	
	/**
	 * 获取数据库连接，并设置为不自动提交
	 * 
	 * 将获取到的连接 放到缓存中
	 * 
	 * @param args canshu
	 */
	public void startMethod(Object[] args) {
		try {

			Map<String, DruidDataSource> maps = (Map<String,DruidDataSource>)easySpace.getAttr(EasyConstant.DATA_SOURCE_MAP);

			Map<String,Connection> connections = new HashMap<>();

			for(String key : maps.keySet()) {
				Connection connection = maps.get(key).getConnection();
				connection.setAutoCommit(false);
				connections.put(key, connection);
			}

			easySpace.setAttr(ThreadUtil.getThreadIdToTraction(), connections);
		} catch (Exception e) {
			logger.error("开启事务出错",e);
		}
	}

	/**
	 * 从缓存中获取当前线程的数据库连接，并提交事务
	 * 
	 * @param args canshu
	 */
	public void endMethod(Object[] args) {
		try {
			Map<String,Connection> connections = (Map<String,Connection>)easySpace.getAttr(ThreadUtil.getThreadIdToTraction());

			for(String key : connections.keySet()) {
				Connection connection = connections.get(key);
				connection.commit();
				connection.close();
			}
		} catch (Exception e) {
			logger.error("提交事务出错",e);
		} finally {
			easySpace.remove(ThreadUtil.getThreadIdToTraction());
		}
		
	}

	/**
	 * 从缓存中获取当前线程的数据库连接，并回滚事务
	 * @param e 异常
	 */
	public void exp(Throwable e) {
		try {
			Map<String,Connection> connections = (Map<String,Connection>)easySpace.getAttr(ThreadUtil.getThreadIdToTraction());

			for(String key : connections.keySet()) {
				Connection connection = connections.get(key);
				connection.rollback();
				connection.close();
			}

			logger.error("",e);
		} catch (Exception ex) {
			logger.error("回滚事务出错",ex);
		} finally {
			easySpace.remove(ThreadUtil.getThreadIdToTraction());
		}
	}

}
