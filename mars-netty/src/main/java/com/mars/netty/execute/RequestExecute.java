package com.mars.netty.execute;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.util.MesUtil;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 处理请求的线程
 * @author yuye
 *
 */
public class RequestExecute {
	
	private Logger log = LoggerFactory.getLogger(RequestExecute.class);

	/**
	 * netty的request对象
	 */
	private FullHttpRequest httpRequest;

	private ChannelHandlerContext ctx;
	
	public void setHttpRequest(FullHttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void execute() {

		/* 组装httpRequest对象 */
		HttpRequest request = new HttpRequest(httpRequest,ctx);

		/* 组装httpResponse对象 */
		HttpResponse response = new HttpResponse(ctx);

		try {
			
			/* 获取全局存储空间 */
			MarsSpace constants = MarsSpace.getEasySpace();
			/* 从存储空间里获取核心servlet的全限名 */
			String className = constants.getAttr("core").toString();
			
			/* 通过反射执行核心servlet */
			Class<?> cls = Class.forName(className);
			Object object = cls.getDeclaredConstructor().newInstance();
			Method helloMethod = cls.getDeclaredMethod("doRequest", new Class[] { HttpRequest.class ,HttpResponse.class});
			Object result = helloMethod.invoke(object, new Object[] { request ,response});
			if(result != null && result.toString().equals(MarsConstant.VOID)) {
				return;
			}
			/* 将控制层返回的数据响应给客户端 */
			response.send(String.valueOf(result));
		} catch (InvocationTargetException e){
			log.error("处理请求的时候出错",e);
			response.send(MesUtil.getMes(500,"处理请求发生错误:"+e.getTargetException().getMessage()).toJSONString(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("处理请求的时候出错",e);
			response.send(MesUtil.getMes(500,"处理请求发生错误:"+e.getMessage()).toJSONString(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try{
				// 释放请求
				ctx.close();
				httpRequest.release();
			} catch (Exception e){
				log.error("释放请求出错",e);
			}
		}
	}
}
