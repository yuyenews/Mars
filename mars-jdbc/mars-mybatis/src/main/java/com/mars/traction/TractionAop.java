package com.mars.traction;

import com.mars.core.constant.EasyConstant;
import com.mars.core.constant.EasySpace;
import com.mars.core.enums.TractionLevel;
import com.mars.core.logger.MarsLogger;
import com.mars.core.model.AopModel;
import com.mars.core.util.ThreadUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 事务管理aop
 * @author yuye
 *
 */
public class TractionAop {

	private MarsLogger logger = MarsLogger.getLogger(TractionAop.class);

	private static EasySpace easySpace = EasySpace.getEasySpace();
	
	/**
	 * 获取数据库连接，并设置为不自动提交
	 * 
	 * 将获取到的连接 放到缓存中
	 * 
	 * @param args canshu
	 */
	public void startMethod(Object[] args, AopModel aopModel) {
		try {
			Map<String,SqlSessionFactory> maps = (Map<String,SqlSessionFactory>)easySpace.getAttr(EasyConstant.DATA_SOURCE_MAP);
			
			Map<String,SqlSession> sqlSessions = new HashMap<>();
			
			for(String key : maps.keySet()) {
				ExecutorType executorType = ExecutorType.valueOf(aopModel.getExecutorType().getCode());
				TransactionIsolationLevel transactionIsolationLevel = TransactionIsolationLevel.valueOf(aopModel.getTractionLevel().getCode());
				SqlSession sqlSession = maps.get(key).openSession(executorType, transactionIsolationLevel);
				sqlSessions.put(key, sqlSession);
			}

			easySpace.setAttr(ThreadUtil.getThreadIdToTraction(), sqlSessions);
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
			Map<String,SqlSession> sqlSessions = (Map<String,SqlSession>)easySpace.getAttr(ThreadUtil.getThreadIdToTraction());

			for(String key : sqlSessions.keySet()) {
				SqlSession session = sqlSessions.get(key);
				session.commit();
				session.close();
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
			Map<String,SqlSession> sqlSessions = (Map<String,SqlSession>)easySpace.getAttr(ThreadUtil.getThreadIdToTraction());

			for(String key : sqlSessions.keySet()) {
				SqlSession session = sqlSessions.get(key);
				session.rollback();
				session.close();
			}

			logger.error("",e);
		} catch (Exception ex) {
			logger.error("回滚事务出错",ex);
		} finally {
			easySpace.remove(ThreadUtil.getThreadIdToTraction());
		}
	}
}
