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
	 * 加载所有bean，包括controller 的class对象
	 * @param packageName 包名
	 *
	 * @throws Exception 异常
	 */
	public static void sacnClass(String packageName) throws Exception{
		try {

			Set<String> scanClassList = LoadHelper.getSacnClassList();

			/* 扫描包下面的所有类 */
			Set<String> classList = ReadClass.loadClassList(packageName);

			/* 加载本地bean */
			Set<String> navClassList = loadNativeBeans();

			/* 将扫描出来的类保存到内存中 */
			scanClassList.addAll(classList);
			scanClassList.addAll(navClassList);

			marsSpace.setAttr(MarsConstant.SCAN_ALL_CLASS,classList);

		} catch (Exception e){
			throw new Exception("扫描["+packageName+"]包下的类发生错误",e);
		}
	}

	/**
	 * 加载本地bean
	 * @throws Exception 异常
	 */
	public static Set<String> loadNativeBeans() throws Exception {
		Set<String> navClassList = new HashSet<>();

		/* 加载 接受远程配置中心通知的controller */
		navClassList.add("com.mars.mvc.remote.controller.RemoteConfigController");

		return navClassList;
	}
}
