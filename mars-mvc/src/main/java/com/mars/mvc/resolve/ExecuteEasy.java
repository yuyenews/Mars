package com.mars.mvc.resolve;

import com.mars.mvc.base.BaseInterceptor;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.MesUtil;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import com.mars.server.util.RequestUtil;
import com.mars.mvc.resolve.model.EasyMappingModel;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 执行器
 * 
 * @author yuye
 *
 */
public class ExecuteEasy {

	private MarsLogger log = MarsLogger.getLogger(ExecuteEasy.class);

	private static ExecuteEasy executeEasy;

	private ExecuteEasy() {
	}

	public static ExecuteEasy getExecuteEasy() {
		if (executeEasy == null) {
			executeEasy = new ExecuteEasy();
		}
		return executeEasy;
	}

	/**
	 * 执行controller
	 * @param easyMappingModel duix
	 * @param method fangfa
	 * @param request qingqiu
	 * @param response xiangying
	 * @return duix
	 */
	public Object execute(EasyMappingModel easyMappingModel, HttpMethod method, HttpRequest request, HttpResponse response) {

		try {
			
			if(easyMappingModel == null) {
				return MesUtil.getMes(404,"服务器上没有相应的接口");
			}
			
			String strMethod = method.name().toString().toLowerCase();

			String mathodReuest = easyMappingModel.getRequestMetohd().name().toLowerCase();
			
			if (strMethod.equals(mathodReuest)) {

				/* 获取拦截器 并执行 控制层执行前的方法 */
				String uriEnd = RequestUtil.getUriName(request);
				List<Object> list = ExecuteInters.getInters(uriEnd);
				Object inres = ExecuteInters.executeIntersStart(list,request, response);
				if(!inres.toString().equals(BaseInterceptor.SUCCESS)) {
					return inres;
				}

				/* 获取要执行的controller的信息 */
				Object obj = easyMappingModel.getObject();
				Class<?> cls = easyMappingModel.getCls();
				Method method2 = cls.getDeclaredMethod(easyMappingModel.getMethod(), new Class[] { HttpRequest.class, HttpResponse.class });

				/* 获取controller返回值的类型 */
				Class cl = method2.getReturnType();
				String st = cl.getName();

				Object result = null;
				if(st.toLowerCase().trim().equals("void")){
					method2.invoke(obj, new Object[] { request, response });
					result = "void405cb55d6781877e9e930aa8e046098b";
				} else {
					result = method2.invoke(obj, new Object[] { request, response });
				}

				/* 执行拦截器 在控制层执行后的方法 */
				Object inres2 = ExecuteInters.executeIntersEnd(list,request, response,result);
				if(!inres2.toString().equals(BaseInterceptor.SUCCESS)) {
					return inres2;
				}
				
				return result;
			} else {
				/* 如果请求方式和controller的映射不一致，则提示客户端 */
				return MesUtil.getMes(403,"此接口的请求方式为[" + mathodReuest + "]");
			}
		} catch (Exception e) {
			log.error("执行控制层的时候报错",e);
			return MesUtil.getMes(500,"执行控制层的时候报错");
		}
	}
	
}
