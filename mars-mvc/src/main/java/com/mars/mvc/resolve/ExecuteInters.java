package com.mars.mvc.resolve;

import com.mars.mvc.base.BaseInterceptor;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.MatchUtil;
import com.mars.common.util.MesUtil;
import com.mars.mvc.load.model.MarsInterModel;
import com.mars.aio.server.request.HttpMarsRequest;
import com.mars.aio.server.request.HttpMarsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行拦截器
 * @author yuye
 *
 */
public class ExecuteInters {

	private static Logger logger = LoggerFactory.getLogger(ExecuteInters.class);

	/**
	 * 拦截器缓存，按照请求的URL分好类
	 */
	private static Map<String, List<MarsInterModel>> interList = new ConcurrentHashMap<>();

	/**
	 * 执行拦截器的开始方法
	 *
	 * @param list     拦截器集合
	 * @param request  请求对象
	 * @param response 响应对象
	 * @return 拦截器的返回数据
	 */
	public static Object executeIntersStart(List<MarsInterModel> list, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
		Class className = null;
		try {
			for (MarsInterModel marsInterModel : list) {
				className = marsInterModel.getCls();

				BaseInterceptor interceptor = (BaseInterceptor)marsInterModel.getObj();
				Object result = interceptor.beforeRequest(request, response);

				if (result == null || !result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}
			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型[" + className.getName() + "]", e);
			throw new Exception(errorResult(className.getName(), e), e);
		}
	}

	/**
	 * 执行拦截器的结束方法
	 *
	 * @param list      拦截器集合
	 * @param request   请求对象
	 * @param response  响应对象
	 * @param conResult api的返回数据
	 * @return 拦截器的返回数据
	 */
	public static Object executeIntersEnd(List<MarsInterModel> list, HttpMarsRequest request, HttpMarsResponse response, Object conResult) throws Exception {
		Class className = null;
		try {

			for (MarsInterModel marsInterModel : list) {
				className = marsInterModel.getCls();

				BaseInterceptor interceptor = (BaseInterceptor)marsInterModel.getObj();
				Object result = interceptor.afterRequest(request, response, conResult);

				if (result == null || !result.toString().equals(BaseInterceptor.SUCCESS)) {
					return result;
				}
			}

			return BaseInterceptor.SUCCESS;
		} catch (Exception e) {
			logger.error("执行拦截器报错，拦截器类型[" + className.getName() + "]", e);
			throw new Exception(errorResult(className.getName(), e), e);
		}
	}

	/**
	 * 获取所有符合条件的拦截器
	 *
	 * @param uriEnd url
	 * @return 拦截器集合
	 */
	public static List<MarsInterModel> getInters(String uriEnd) {
		try {
			/* 先从缓存中直接取，如果有就返回 */
			List<MarsInterModel> list = interList.get(uriEnd);
			if (list != null && list.size() > 0) {
				return list;
			}

			/* 如果缓存中没有，那就从MarsSpace中查找 */
			list = new ArrayList<>();
			Object interceptorsObj = MarsSpace.getEasySpace().getAttr(MarsConstant.INTERCEPTOR_OBJECTS);
			if (interceptorsObj == null) {
				return list;
			}

			/* 遍历拦截器，一个个的匹配 */
			List<MarsInterModel> interceptors = (List<MarsInterModel>) interceptorsObj;
			for (MarsInterModel marsInterModel : interceptors) {
				if (MatchUtil.isMatch(marsInterModel.getPattern(), uriEnd.toUpperCase())) {
					if (hasExclude(marsInterModel, uriEnd)) {
						/* 如果此url在此拦截器的排除名单中，则不进行拦截 */
						continue;
					}
					list.add(marsInterModel);
				}
			}
			interList.put(uriEnd, list);
			return list;
		} catch (Exception e) {
			logger.error("读取拦截器报错", e);
			return new ArrayList<>();
		}
	}

	/**
	 * 判断拦截器是否需要拦截此接口
	 *
	 * @param marsInterModel
	 * @return
	 * @throws Exception
	 */
	private static Boolean hasExclude(MarsInterModel marsInterModel, String uriEnd) throws Exception {
		BaseInterceptor interceptor = (BaseInterceptor)marsInterModel.getObj();
		List<String> excludeList = interceptor.exclude();
		if (excludeList == null || excludeList.size() < 1) {
			return false;
		}

		Boolean hasExc = excludeList.contains(uriEnd);
		return hasExc;
	}

	/**
	 * 返回错误信息
	 *
	 * @param className 拦截器类名
	 * @return
	 */
	private static String errorResult(String className, Exception e) {
		String jsonString = MesUtil.getMes(500, "执行拦截器报错，拦截器类型[" + className + "]," + e.getMessage());
		return jsonString;
	}
}
