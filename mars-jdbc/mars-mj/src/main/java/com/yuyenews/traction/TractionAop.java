package com.yuyenews.traction;

import com.yuyenews.aop.base.BaseAop;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.logger.MarsLogger;
import com.yuyenews.core.util.ThreadUtil;

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
	@SuppressWarnings("unchecked")
	public void startMethod(Object[] args) {
		try {

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


			logger.error("",e);
		} catch (Exception ex) {
			logger.error("回滚事务出错",ex);
		} finally {
			easySpace.remove(ThreadUtil.getThreadIdToTraction());
		}
	}

}
