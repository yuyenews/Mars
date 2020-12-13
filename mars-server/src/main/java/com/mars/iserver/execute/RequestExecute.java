package com.mars.iserver.execute;

import com.mars.common.ncfg.mvc.CoreServletClass;
import com.mars.common.util.MesUtil;
import com.mars.common.util.StringUtil;
import com.mars.iserver.par.factory.InitRequestFactory;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.servlet.MarsServlet;
import com.mars.server.util.RequestUtil;
import com.mars.iserver.execute.access.PathAccess;
import com.mars.iserver.par.factory.ParamAndResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * 处理请求的线程
 * @author yuye
 *
 */
public class RequestExecute {

	private Logger log = LoggerFactory.getLogger(RequestExecute.class);

	/**
	 * 执行请求
	 */
	public void execute(HttpMarsRequest request, HttpMarsResponse response) {
		try {
			Object result = "ok";
			/* 如果请求路径合法，则继续往下执行 */
			String uri = RequestUtil.getUriName(request);
			if(!PathAccess.hasAccess(uri)){
				/* 如果是合法请求，那就获取请求的参数数据，并填充request对象 */
				request = InitRequestFactory.getInitRequest().getHttpMarsRequest(request);

				/* 执行核心控制器 */
				MarsServlet marsServlet = (MarsServlet)CoreServletClass.getObject();
				result = marsServlet.doRequest(request, response);
			}

			/* 响应 */
			ParamAndResultFactory.getBaseParamAndResult().result(response, result);
		} catch (Exception e) {
			log.error("处理请求的时候出错", e);
			String msg = getErrorMsg(e);
			response.send(MesUtil.getMes(500, msg).toJSONString());
		}
	}

	/**
	 * 处理异常信息
	 * @param e 异常对象
	 * @return 信息
	 */
	private String getErrorMsg(Exception e) {
		String msg = null;
		if(e instanceof InvocationTargetException){
			InvocationTargetException invocationTargetException = (InvocationTargetException)e;
			msg = invocationTargetException.getTargetException().getMessage();
		} else {
			msg = e.getMessage();
		}
		if (StringUtil.isNull(msg) || msg.trim().toUpperCase().equals("NULL")) {
			msg = "服务端出现异常,请查看日志以及检查您的代码进行排查";
		}
		return msg;
	}
}
