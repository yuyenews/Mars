package com.mars.aio.execute;

import com.mars.common.ncfg.mvc.DispatcherFactory;
import com.mars.common.util.MesUtil;
import com.mars.common.util.StringUtil;
import com.mars.aio.par.factory.InitRequestFactory;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.dispatcher.MarsDispatcher;
import com.mars.server.util.RequestUtil;
import com.mars.aio.execute.access.PathAccess;
import com.mars.aio.par.factory.ParamAndResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * 处理请求的线程
 * @author yuye
 *
 */
public class RequestExecute {

	private static Logger log = LoggerFactory.getLogger(RequestExecute.class);

	/**
	 * 执行请求
	 */
	public static void execute(HttpMarsRequest request, HttpMarsResponse response) {
		try {
			Object result = "ok";
			/* 如果请求路径合法，则继续往下执行 */
			String uri = RequestUtil.getUriName(request);
			if(!PathAccess.hasAccess(uri)){
				/* 如果是合法请求，那就获取请求的参数数据，并填充request对象 */
				request = InitRequestFactory.getInitRequest().getHttpMarsRequest(request);

				/* 执行核心控制器 */
				MarsDispatcher marsDispatcher = (MarsDispatcher) DispatcherFactory.getDispatcher();
				result = marsDispatcher.doRequest(request, response);
			}

			/* 响应 */
			ParamAndResultFactory.getBaseParamAndResult().result(response, result);
		} catch (Exception e) {
			log.error("处理请求的时候出错", e);
			String msg = getErrorMsg(e);
			response.send(MesUtil.getMes(500, msg));
		}
	}

	/**
	 * 处理异常信息
	 * @param e 异常对象
	 * @return 信息
	 */
	private static String getErrorMsg(Exception e) {
		String msg = null;
		try {
			if(e instanceof InvocationTargetException){
				InvocationTargetException invocationTargetException = (InvocationTargetException)e;
				Throwable obj = invocationTargetException.getTargetException().getCause();
				msg = obj.getClass().getName() + ":" + obj.getMessage();
			} else {
				msg = e.getMessage();
				if (!StringUtil.isNull(msg) && !msg.trim().toUpperCase().equals("NULL")) {
					msg = e.getClass().getName() + ":" + msg;
				}
			}

			if (StringUtil.isNull(msg) || msg.trim().toUpperCase().equals("NULL")) {
				msg = "服务端出现异常,请查看日志以及检查您的代码进行排查";
			}
		} catch (Exception ex){
			msg = "服务端出现异常,请查看日志以及检查您的代码进行排查";
		}
		return msg;
	}
}
