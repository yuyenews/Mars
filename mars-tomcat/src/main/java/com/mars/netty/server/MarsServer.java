package com.mars.netty.server;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tomcat服务
 * @author yuye
 *
 */
public class MarsServer {

	private static Logger log = LoggerFactory.getLogger(MarsServer.class);

	/**
	 * 获取全局存储空间
	 */
	private static MarsSpace constants = MarsSpace.getEasySpace();
	
	/**
	 * 启动tomcat服务
	 * @param portNumber
	 */
	public static void start(final int portNumber) {
		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(portNumber);
			tomcat.setBaseDir(".");

			final Context context = tomcat.addContext("/", null);
			Tomcat.addServlet(context, "dispatcher", new MarsServerHandler());
			context.addServletMappingDecoded("/*", "dispatcher");

			tomcat.init();
			tomcat.start();

			log.info("启动成功");
			/* 标识tomcat是否已经启动 */
			constants.setAttr(MarsConstant.HAS_NETTY_START,"yes");

			tomcat.getServer().await();

		} catch (Exception e) {
			log.error("启动netty报错",e);
		}
	}
}
