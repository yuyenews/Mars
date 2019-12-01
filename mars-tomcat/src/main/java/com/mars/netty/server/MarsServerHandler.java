package com.mars.netty.server;

import com.mars.netty.execute.RequestExecute;
import com.mars.netty.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接收netty服务
 * @author yuye
 *
 */
@MultipartConfig
public class MarsServerHandler extends HttpServlet {

	private Logger log = LoggerFactory.getLogger(MarsServerHandler.class);


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");

			RequestExecute requestExecute = new RequestExecute();
			requestExecute.setHttpRequest(req);
			requestExecute.setHttpResponse(resp);
			requestExecute.execute();

		} catch (Exception e) {
			log.error("处理请求失败!", e);
			ResponseUtil.sendServerError(resp,"处理请求发生错误"+e.getMessage());
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
}
