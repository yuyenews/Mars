package com.mars.iserver.server;

import com.mars.iserver.execute.RequestExecute;
import com.mars.iserver.util.ResponseUtil;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.request.impl.HttpMarsDefaultRequest;
import com.mars.server.server.request.impl.HttpMarsDefaultResponse;
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
		/* 组装httpRequest对象 */
		HttpMarsRequest request = new HttpMarsDefaultRequest(httpExchange);;

		/* 组装httpResponse对象 */
		HttpMarsResponse response = new HttpMarsDefaultResponse(httpExchange);

		try {
			RequestExecute requestExecute = new RequestExecute();
			requestExecute.execute(request, response);
		} catch (Exception e) {
			log.error("处理请求失败!", e);
			ResponseUtil.sendServerError(response,"处理请求发生错误"+e.getMessage());
		}
	}
}
