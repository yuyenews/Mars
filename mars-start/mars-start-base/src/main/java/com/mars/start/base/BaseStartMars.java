package com.mars.start.base;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.after.StartAfter;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadClass;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.ConfigUtil;
import com.mars.junit.StartList;
import com.mars.mvc.load.LoadInters;
import com.mars.netty.server.MarsServer;
import com.mars.ioc.load.LoadEasyBean;
import com.mars.jdbc.base.BaseInitJdbc;
import com.mars.mvc.load.LoadController;
import com.mars.mvc.servlet.MarsCoreServlet;
import com.mars.timer.execute.ExecuteMarsTimer;
import com.mars.timer.load.LoadMarsTimer;

import java.util.List;

/**
 * 启动Mars框架
 * @author yuye
 *
 */
public class BaseStartMars {
	
	private static MarsLogger log = MarsLogger.getLogger(BaseStartMars.class);
	
	/**
	 * 获取全局存储空间 
	 */
	private static MarsSpace constants = MarsSpace.getEasySpace();

	/**
	 * 启动Mars框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz, BaseInitJdbc baseInitJdbc, String suffix, List<StartList> startList) {
		try {
			
			log.info("程序启动中......");

			/* 加载框架数据 */
			load(clazz,baseInitJdbc,suffix);

			/* 扩展要加载的东西 */
			if(startList != null){
				for(StartList item : startList){
					item.load();
				}
			}

			/* 启动after方法 */
			StartAfter.after();

			/* 执行定时任务 */
			ExecuteMarsTimer.execute();

			/* 启动netty */
			MarsServer.start(getPort());

		} catch (Exception e) {
			log.error("",e);
			System.exit(0);
		}
	}
	
	/**
	 * 加载所需的资源
	 */
	private static void load(Class<?> clazz, BaseInitJdbc baseInitJdbc,String suffix) throws Exception{
		
		/* 配置核心servlet */
		constants.setAttr("core", MarsCoreServlet.class.getName());
		
		/* 加载配置文件 */
		ConfigUtil.loadConfig(suffix);
		
		/*获取要扫描的包*/
		String className = getClassName(clazz);
		
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

		/* 创建interceptor对象 */
		LoadInters.loadIntersList();

		/* 标识createBean方法已经调用完毕 */
		constants.setAttr(MarsConstant.HAS_START,"yes");

		/* 加载timer对象 */
		LoadMarsTimer.loadMarsTimers();
	}

	/**
	 * 截取main方法所在包名
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private static String getClassName(Class clazz) throws Exception {
		String className = clazz.getName();
		if(className.indexOf(".") < 0){
			throw new Exception("启动服务的main方法所在的类,必须放在两层包名中,比如[com.mars,com.test]等,不允许放在[com,cn]等包中,更不允许放在包外面");
		}
		return className.substring(0,className.lastIndexOf("."));
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
