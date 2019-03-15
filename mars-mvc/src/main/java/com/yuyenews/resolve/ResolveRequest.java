package com.yuyenews.resolve;

import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.logger.MarsLogger;
import com.yuyenews.core.util.MesUtil;
import com.yuyenews.easy.server.request.HttpRequest;
import com.yuyenews.easy.server.request.HttpResponse;
import com.yuyenews.easy.util.RequestUtil;
import com.yuyenews.resolve.model.EasyMappingModel;

import java.util.Map;

/**
 * 解析请求
 * @author yuye
 *
 */
public class ResolveRequest {
	
	private static MarsLogger log = MarsLogger.getLogger(ResolveRequest.class);

	private static ResolveRequest resolveRequest;
	
	private EasySpace constants = EasySpace.getEasySpace();
	
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
	public Object resolve(HttpRequest request,HttpResponse response) {
		
		try {
			Map<String,EasyMappingModel> maps = getControllers();
			
			String uri = getRequestPath(request);
			
			return executeEasy.execute(maps.get(uri),request.getMethod(),request,response);
		} catch (Exception e) {
			log.error("解释请求的时候报错",e);
		}
		return MesUtil.getMes(500,"解析请求报错");
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
	@SuppressWarnings("unchecked")
	private Map<String,EasyMappingModel> getControllers() {
		
		Map<String,EasyMappingModel> controlObjects = null;
		Object obj = constants.getAttr("controlObjects");
		if(obj != null) {
			controlObjects = (Map<String,EasyMappingModel>)obj;
		}
		
		return controlObjects;
	}
}
