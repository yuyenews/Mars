package com.mars.common.util;

/**
 * 字符串工具类
 * @author yuye
 *
 */
public class StringUtil {

	/**
	 * 将字符串首字母转成小写
	 * @param str 参数
	 * @return string
	 */
	public static String getFirstLowerCase(String str) {
		return str.substring(0,1).toLowerCase()+str.substring(1);
	}
	
	/**
	 * 判断字符串是否为空
	 * @param obj 参数
	 * @return string
	 */
	public static boolean isNull(Object obj) {
		return obj == null || obj.toString().trim().equals("");
	}
}
