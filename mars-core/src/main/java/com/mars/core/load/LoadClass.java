package com.mars.core.load;

import com.mars.core.annotation.*;
import com.yuyenews.core.annotation.*;
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
	 * @param packageName
	 */
	public static void loadBeans(String packageName) throws Exception{
		try {
			/* 加载本地bean */
			LoadNactive.loadNactiveBeans();

			/* 加载框架用户的所有bean */
			loadAllBeans(packageName);
		} catch (Exception e){
			throw new Exception("加载bean出错",e);
		}
	}

	/**
	 * 加载所有的bean，包括controller 的class对象
	 * @param packageName bean所在的包名
	 */
	private static void loadAllBeans(String packageName) throws Exception {
		try {
			Set<String> classList = ReadClass.loadClassList(packageName);
			for (String str : classList) {
				Class<?> cls = Class.forName(str);
				Controller controller = cls.getAnnotation(Controller.class);
				EasyBean easyBean = cls.getAnnotation(EasyBean.class);
				EasyInterceptor easyInterceptor = cls.getAnnotation(EasyInterceptor.class);
				EasyDao easyDao = cls.getAnnotation(EasyDao.class);
				EasyAfter easyAfter = cls.getAnnotation(EasyAfter.class);

				if(controller != null) {
					LoadNactive.loadController(cls, controller);
				}
				if(easyBean != null) {
					LoadNactive.loadEasyBean(cls, easyBean);
				}
				if(easyInterceptor != null){
					LoadNactive.loadInterceptor(cls,easyInterceptor);
				}
				if(easyDao != null){
					LoadNactive.loadDao(cls,easyDao);
				}
				if(easyAfter != null){
					LoadNactive.loadEasyAfter(cls);
				}
			}
		} catch (Exception e) {
			throw new Exception("扫描["+packageName+"]包下的类发生错误",e);
		}

	}
	


}
