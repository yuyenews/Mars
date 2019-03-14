package com.yuyenews.core.util;

/**
 * 判断字符串是否与规则匹配
 * @author yuye
 *
 */
public class MatchUtil {

	/**
	 * 判断带通配符的字符串与另一个字符串是否匹配
	 * @param rule 规则
	 * @param str 字符串
	 * @return boolean
	 */
	public static Boolean isMatch(String rule,String str){
		if(rule==null || str == null){
			return false;
		}
		int ind = rule.indexOf("*");
		if(ind>-1){
			if(rule.length()==1){
				return true;
			}else{
				String ru = rule.replaceAll("\\*", "([a-zA-Z1-9]+)");
				return str.matches(ru);
			}
		}else{
			return rule.equals(str);
		}
	}
}
