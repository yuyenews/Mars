package com.mars.mvc.resolve;

import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.util.RequestUtil;
import com.mars.mvc.model.MarsMappingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析请求
 * @author yuye
 *
 */
public class ResolveRequest {
	
	private Logger log = LoggerFactory.getLogger(ResolveRequest.class);

	private static ResolveRequest resolveRequest = new ResolveRequest();
	
	private MarsSpace constants = MarsSpace.getEasySpace();
	
	/**
	 * 执行器对象
	 */
	private ExecuteMars executeMars = ExecuteMars.getExecuteMars();
	
	private ResolveRequest() {}
	
	public static ResolveRequest getResolveRequest() {
		return resolveRequest;
	}
	
	/**
	 * 解释请求，并调用对应的控制层方法进行处理
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public Object resolve(HttpMarsRequest request, HttpMarsResponse response) throws Exception {
		
		try {
			Map<String, MarsMappingModel> maps = getMarsApis();
			String uri = getRequestPath(request).toUpperCase();
			return executeMars.execute(maps.get(uri),request.getMethod(),request,response);
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
	private String getRequestPath(HttpMarsRequest request) {
		/* 获取路径 */
		String uri = RequestUtil.getUriName(request);
		if(uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		return uri;
	}
	
	/**
	 * 获取所有的MarsApi对象
	 * @return duix
	 */
	private Map<String, MarsMappingModel> getMarsApis() {
		Object obj = constants.getAttr(MarsConstant.CONTROLLER_OBJECTS);
		return obj != null ? (Map<String, MarsMappingModel>) obj : Collections.emptyMap();
	}
}
