package com.yuyenews.core.util;

/**
 * 线程工具类
 * @author yuye
 *
 */
public class ThreadUtil {
	
	/**
	 * 获取当前线程的ID
	 * @return id
	 */
	public static String getThreadIdToTraction() {
		return getThreadId("traction");
	}

	/**
	 * 获取当前线程的ID
	 * @param tag
	 * @return id
	 */
	public static String getThreadId(String tag) {
		return String.valueOf(Thread.currentThread().getId())+tag;
	}
	
	/**
	 * 获取当前线程的ID
	 * @return id
	 */
	public static String getThreadId() {
		return String.valueOf(Thread.currentThread().getId());
	}
	
}
