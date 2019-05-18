package com.mars.core.load;

import com.mars.core.annotation.*;
import com.mars.core.util.ReadClass;

import java.util.Set;

/**
 * 获取项目中的所有class
 * 
 * @author yuye
 *
 */
public class LoadClass {
	
	/**
	 * 加载所有bean，包括controller 的class对象
	 * @param packageName 包名
	 *
	 * @throws Exception 异常
	 */
	public static void loadBeans(String packageName) throws Exception{
		try {
			/* 加载本地bean */
			LoadBeans.loadNativeBeans();

			/* 加载框架用户的所有bean */
			loadAllBeans(packageName);
		} catch (Exception e){
			throw new Exception("加载bean出错",e);
		}
	}

	/**
	 * 加载所有的bean，包括controller 的class对象
	 * @param packageName bean所在的包名
	 *
	 * @throws Exception 异常
	 */
	private static void loadAllBeans(String packageName) throws Exception {
		try {
			Set<String> classList = ReadClass.loadClassList(packageName);
			for (String str : classList) {
				Class<?> cls = Class.forName(str);
				Controller controller = cls.getAnnotation(Controller.class);
				MarsBean marsBean = cls.getAnnotation(MarsBean.class);
				MarsInterceptor marsInterceptor = cls.getAnnotation(MarsInterceptor.class);
				MarsDao marsDao = cls.getAnnotation(MarsDao.class);
				MarsAfter marsAfter = cls.getAnnotation(MarsAfter.class);

				if(controller != null) {
					LoadBeans.loadController(cls, controller);
				}
				if(marsBean != null) {
					LoadBeans.loadEasyBean(cls, marsBean);
				}
				if(marsInterceptor != null){
					LoadBeans.loadInterceptor(cls, marsInterceptor);
				}
				if(marsDao != null){
					LoadBeans.loadDao(cls, marsDao);
				}
				if(marsAfter != null){
					LoadBeans.loadEasyAfter(cls);
				}
			}
		} catch (Exception e) {
			throw new Exception("扫描["+packageName+"]包下的类发生错误",e);
		}
	}
}
