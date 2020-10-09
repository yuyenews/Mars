package com.mars.server.server.request;

import com.alibaba.fastjson.JSONObject;
import com.mars.server.server.request.model.MarsFileUpLoad;
import java.util.*;

/**
 * 请求对象
 * @author yuye
 *
 */
public interface HttpMarsRequest {

	/**
	 * 获取java原生request
	 * @return 原生请求对象
	 */
	<T> T getNativeRequest(Class<T> cls);

	/**
	 * 添加上传的文件
	 * @param files
	 */
	void setFiles(Map<String,MarsFileUpLoad> files);

	/**
	 * 获取json传参
	 * @return json参数
	 */
	JSONObject getJsonParam();

	/**
	 * 设置json传参
	 * @param jsonParam
	 */
	void setJsonParam(JSONObject jsonParam);

	/**
	 * 获取参数类型
	 * @return 参数类型
	 */
	String getContentType();

	/**
	 * 设置参数
	 * @param params
	 */
	void setParams(Map<String, List<String>> params);

	/**
	 * 获取请求方法
	 * @return 请求方法
	 */
	String getMethod();

	/**
	 * 获取要请求的uri
	 * @return 请求方法
	 */
	String getUrl();
	
	/**
	 * 获取请求头数据
	 * @param key 键
	 * @return 头数据
	 */
	String getHeader(String key);

	/**
	 * 获取请求头数据
	 * @param key 键
	 * @return 头数据
	 */
	List<String> getHeaders(String key);

	/**
	 * 获取请求的参数集
	 * @return 请求参数
	 */
	Map<String, Object> getParameters();

	/**
	 * 获取单个请求的参数
	 * @param key 键
	 * @return 请求参数
	 */
	String getParameter(String key);
	
	/**
	 * 获取单个请求的参数
	 * @param key 键
	 * @return 请求参数
	 */
	String[] getParameterValues(String key);

	/**
	 * 获取请求的文件
	 * @return 文件列表
	 */
	Map<String, MarsFileUpLoad> getFiles() throws Exception;

	/**
	 * 获取单个请求的文件
	 * 
	 * @param name 名称
	 * @return 单个文件
	 */
	MarsFileUpLoad getFile(String name) throws Exception;
	
	/**
	 * 获取客户端InetSocketAddress
	 * @return inetSocketAddress
	 */
	String getInetSocketAddress();
}
