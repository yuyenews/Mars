package com.yuyenews.start.base;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.after.StartAfter;
import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.load.LoadClass;
import com.yuyenews.core.logger.MarsLogger;
import com.yuyenews.core.util.ConfigUtil;
import com.yuyenews.netty.server.EasyServer;
import com.yuyenews.ioc.load.LoadEasyBean;
import com.yuyenews.jdbc.base.BaseInitJdbc;
import com.yuyenews.resolve.LoadController;
import com.yuyenews.servlcet.EasyCoreServlet;

/**
 * 启动easy框架
 * @author yuye
 *
 */
public class BaseStartEasy {
	
	private static MarsLogger log = MarsLogger.getLogger(BaseStartEasy.class);
	
	/**
	 * 获取全局存储空间 
	 */
	private static EasySpace constants = EasySpace.getEasySpace();

	/**
	 * 启动easy框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz, BaseInitJdbc baseInitJdbc) {
		try {
			
			log.info("程序启动中......");

			/* 加载框架数据 */
			load(clazz,baseInitJdbc);

			/* 标识createbean方法已经调用完毕 */
			constants.setAttr(EasyConstant.HAS_START,"yes");

			/* 启动after方法 */
			StartAfter.after();

			/* 启动netty */
			EasyServer.start(getPort());

		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	/**
	 * 加载控制层所有的类和所需数据
	 */
	private static void load(Class<?> clazz, BaseInitJdbc baseInitJdbc) throws Exception{
		
		/* 配置核心servlet */
		constants.setAttr("core", EasyCoreServlet.class.getName());
		
		/* 加载配置文件 */
		ConfigUtil.loadConfig();
		
		/*获取要扫描的包*/
		String className = clazz.getName();
		className = className.substring(0,className.lastIndexOf("."));
		
		/* 将要扫描的包名存到全局存储空间，给别的需要的地方使用 */
		constants.setAttr("rootPath", className);
		
		/* 获取此包下面的所有类（包括jar中的） */
		LoadClass.loadBeans(className);

		/* 加载JDBC模块 */
        if(baseInitJdbc != null){
            baseInitJdbc.init();
        }
		
		/* 创建bean对象 */
		LoadEasyBean.loadBean();
		
		/* 创建controller对象 */
		LoadController.loadContrl();
		
	}
	
	/**
	 * 获取端口号，默认8080
	 * @return
	 */
	private static int getPort() {

		JSONObject jsonObject = ConfigUtil.getConfig();
		Object por = jsonObject.get("port");
		if(por!=null) {
			return Integer.parseInt(por.toString());
		}
		
		return 8080;
	}

}
