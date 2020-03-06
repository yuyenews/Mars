package com.mars.netty.execute;

import com.mars.core.ncfg.mvc.CoreServletClass;
import com.mars.core.util.MesUtil;
import com.mars.core.util.StringUtil;
import com.mars.netty.par.factory.ParamAndResultFactory;
import com.mars.netty.util.FileUpLoad;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.netty.util.FileItemUtil;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 处理请求的线程
 * @author yuye
 *
 */
public class RequestExecute {

	private Logger log = LoggerFactory.getLogger(RequestExecute.class);

	/**
	 * tomcat的request对象
	 */
	private HttpServletRequest httpRequest;

	/**
	 * tomcat的response对象
	 */
	private HttpServletResponse httpResponse;

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public void setHttpResponse(HttpServletResponse response) {
		this.httpResponse = response;
	}

	/**
	 * 执行请求
	 */
	public void execute() {

		/* 组装httpRequest对象 */
		HttpMarsRequest request = new HttpMarsRequest(httpRequest);

		/* 组装httpResponse对象 */
		HttpMarsResponse response = new HttpMarsResponse(httpResponse);

		try {
			/* 从请求中获取数据 */
			List<FileItem> fileItemList = FileUpLoad.getFileItem(httpRequest);
			/* 请求的数据中分出表单数据和文件流 */
			request = FileItemUtil.getHttpMarsRequest(fileItemList, request);

			/* 通过反射执行核心servlet */
			Class<?> cls = CoreServletClass.getCls();
			Object object = cls.getDeclaredConstructor().newInstance();
			Method helloMethod = cls.getDeclaredMethod("doRequest", new Class[]{HttpMarsRequest.class, HttpMarsResponse.class});
			Object result = helloMethod.invoke(object, new Object[]{request, response});

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
			msg = "服务端出现不明异常,请查看日志以及检查您的代码进行排查";
		}
		return msg;
	}
}
