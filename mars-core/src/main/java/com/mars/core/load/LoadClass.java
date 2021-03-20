package com.mars.core.load;

import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.JSONUtil;
import com.mars.common.util.ReadClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 获取项目中的所有class
 * 
 * @author yuye
 *
 */
public class LoadClass {

	private static MarsSpace marsSpace = MarsSpace.getEasySpace();
	
	/**
	 * 加载所有bean，包括MarsApi 的class对象
	 * @param packageName 包名
	 *
	 * @throws Exception 异常
	 */
	public static void scanClass(List<String> packageName) throws Exception{
		try {
			/* 扫描包下面的所有类 */
			Set<String> scanClassList = scanClassList(packageName);

			/* 加载本地bean */
			Set<String> navClassList = loadNativeClass();

			/* 将框架自有的Bean和扫描出来的类合并到一个集合 */
			scanClassList.addAll(navClassList);

			/* 将扫描出来的类保存到内存中 */
			marsSpace.setAttr(MarsConstant.SCAN_ALL_CLASS,scanClassList);

		} catch (Exception e){
			throw new Exception("扫描["+ JSONUtil.toJSONString(packageName) +"]包下的类发生错误",e);
		}
	}

	/**
	 * 扫描框架的类
	 * @param packageName 要扫描的包名
	 * @return 扫描出来的包
	 * @throws Exception 异常
	 */
	private static Set<String> scanClassList(List<String> packageName) throws Exception {
		Set<String> scanClassList = LoadHelper.getSacnClassList();
		for(String pkName : packageName){
			Set<String> classList = ReadClass.loadClassList(pkName);
			scanClassList.addAll(classList);
		}
		return scanClassList;
	}

	/**
	 * 加载本地Class
	 *
	 * @return 类集合
	 * @throws Exception 异常
	 */
	public static Set<String> loadNativeClass() throws Exception {
		Set<String> navClassList = new HashSet<>();

		// 添加RedisTemplate
		navClassList.add("com.mars.redis.template.MarsRedisTemplate");
		// 添加分布式锁帮助类
		navClassList.add("com.mars.redis.lock.MarsRedisLock");

		return navClassList;
	}
}
