package com.mars.netty.thread;

import com.alibaba.fastjson.JSON;
import com.mars.core.constant.EasySpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.MesUtil;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.lang.reflect.Method;

/**
 * 处理请求的线程
 * @author yuye
 *
 */
public class RequestThread implements Runnable {
	
	private MarsLogger log = MarsLogger.getLogger(RequestThread.class);

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

	public void run() {

		/* 组装httprequest对象 */
		HttpRequest request = new HttpRequest(httpRequest,ctx);

		/* 组装httpresponse对象 */
		HttpResponse response = new HttpResponse(ctx);

		try {
			
			/* 获取全局存储空间 */
			EasySpace constants = EasySpace.getEasySpace();
			/* 从存储空间里获取核心servlet的全限名 */
			String className = constants.getAttr("core").toString();
			
			/* 通过反射执行核心servlet */
			Class<?> cls = Class.forName(className);
			Object object = cls.getDeclaredConstructor().newInstance();
			Method helloMethod = cls.getDeclaredMethod("doRequest", new Class[] { HttpRequest.class ,HttpResponse.class});
			Object result = helloMethod.invoke(object, new Object[] { request ,response});
			if(result != null && result.toString().equals("void405cb55d6781877e9e930aa8e046098b")) {
				return;
			}
			/* 将控制层返回的数据，转成json字符串返回 */
			response.send(JSON.toJSONString(result));
			
		} catch (Exception e) {
			log.error("处理请求的时候出错",e);
			response.send(MesUtil.getMes(500,"处理请求发生错误"+e).toJSONString());
		} finally {
			// 释放请求
			httpRequest.release();
		}
	}
}
