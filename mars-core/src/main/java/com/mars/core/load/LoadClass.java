package com.mars.core.load;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.util.ReadClass;

import java.util.HashSet;
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
	public static void scanClass(String packageName) throws Exception{
		try {

			Set<String> scanClassList = LoadHelper.getSacnClassList();

			/* 扫描包下面的所有类 */
			Set<String> classList = ReadClass.loadClassList(packageName);

			/* 加载本地bean */
			Set<String> navClassList = loadNativeClass();

			/* 将框架自有的Bean和扫描出来的类合并到一个集合 */
			scanClassList.addAll(classList);
			scanClassList.addAll(navClassList);

			/* 将扫描出来的类保存到内存中 */
			marsSpace.setAttr(MarsConstant.SCAN_ALL_CLASS,scanClassList);

		} catch (Exception e){
			throw new Exception("扫描["+packageName+"]包下的类发生错误",e);
		}
	}

	/**
	 * 加载本地Class
	 * @throws Exception 异常
	 */
	public static Set<String> loadNativeClass() throws Exception {
		Set<String> navClassList = new HashSet<>();

		// 扩展用的，后期用来添加框架自有的Bean

		return navClassList;
	}
}
