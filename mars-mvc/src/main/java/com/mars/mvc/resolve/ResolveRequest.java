package com.mars.mvc.resolve;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.logger.MarsLogger;
import com.mars.mvc.resolve.access.PathAccess;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import com.mars.server.util.RequestUtil;
import com.mars.mvc.model.MarsMappingModel;

import java.util.Map;

/**
 * 解析请求
 * @author yuye
 *
 */
public class ResolveRequest {
	
	private static MarsLogger log = MarsLogger.getLogger(ResolveRequest.class);

	private static ResolveRequest resolveRequest;
	
	private MarsSpace constants = MarsSpace.getEasySpace();
	
	/**
	 * 执行器对象
	 */
	private ExecuteEasy executeEasy = ExecuteEasy.getExecuteEasy();
	
	private ResolveRequest() {}
	
	public static ResolveRequest getResolveRequest() {
		if(resolveRequest == null) {
			resolveRequest = new ResolveRequest();
		}
		return resolveRequest;
	}
	
	/**
	 * 解释请求，并调用对应的控制层方法进行处理
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public Object resolve(HttpRequest request,HttpResponse response) throws Exception {
		
		try {
			Map<String, MarsMappingModel> maps = getControllers();
			
			String uri = getRequestPath(request);
			if(PathAccess.hasAccess(uri)){
				return "ok";
			}
			return executeEasy.execute(maps.get(uri),request.getMethod(),request,response);
		} catch (Exception e) {
			log.error("解释请求的时候报错",e);
			throw e;
		}
	}
	
	/**
	 * 从uri中提取 请求连接的最末端，用来匹配控制层映射
	 * @param request qingqiu
	 * @return
	 */
	private String getRequestPath(HttpRequest request) {
		/* 获取路径 */
		String uri = RequestUtil.getUriName(request);
		if(uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		return uri;
	}
	
	/**
	 * 获取所有的controller对象
	 * @return duix
	 */
	private Map<String, MarsMappingModel> getControllers() {
		
		Map<String, MarsMappingModel> controlObjects = null;
		Object obj = constants.getAttr(MarsConstant.CONTROLLER_OBJECTS);
		if(obj != null) {
			controlObjects = (Map<String, MarsMappingModel>)obj;
		}
		
		return controlObjects;
	}
}
