package com.mars.tomcat.server;

import com.mars.tomcat.execute.RequestExecute;
import com.mars.tomcat.util.ResponseUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 接收请求
 * @author yuye
 *
 */
public class MarsServerHandler implements HttpHandler {

	private Logger log = LoggerFactory.getLogger(MarsServerHandler.class);


	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {

			RequestExecute requestExecute = new RequestExecute();
			requestExecute.setHttpExchange(httpExchange);
			requestExecute.execute();

		} catch (Exception e) {
			log.error("处理请求失败!", e);
			ResponseUtil.sendServerError(httpExchange,"处理请求发生错误"+e.getMessage());
		}
	}
}
