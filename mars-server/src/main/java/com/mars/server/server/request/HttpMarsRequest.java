package com.mars.server.server.request;

import com.mars.server.server.request.model.MarsFileUpLoad;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 请求对象，对原生tomcat的request的补充
 * @author yuye
 *
 */
public class HttpMarsRequest {
	
	/**
	 * tomcat原生request
	 */
	private HttpServletRequest httpRequest;
	
	/**
	 * tomcat原生通道
	 */
	private HttpServletResponse response;

	/**
	 * 上传的文件
	 */
	private Map<String, MarsFileUpLoad> files;

	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpRequest
	 * @param response
	 * @param files
	 */
	public HttpMarsRequest(HttpServletRequest httpRequest, HttpServletResponse response,Map<String,MarsFileUpLoad> files) {
		this.httpRequest = httpRequest;
		this.response = response;
		this.files = files;
	}
	
	/**
	 * 获取请求方法
	 * @return 请求方法
	 */
	public String getMethod() {
		return httpRequest.getMethod();
	}

	/**
	 * 获取要请求的uri
	 * @return 请求方法
	 */
	public String getUrl() {
		return httpRequest.getRequestURL().toString();
	}
	
	/**
	 * 获取请求头数据
	 * @param key 键
	 * @return 头数据
	 */
	public Object getHeader(String key) {
		return httpRequest.getHeader(key);
	}

	/**
	 * 获取请求的参数集
	 * @return 请求参数
	 */
	public Map<String, Object> getParameters() {
		Map<String, Object> params = new HashMap<>();

		Map<String,String[]> parameterMap = httpRequest.getParameterMap();
		for(String key : parameterMap.keySet()){
			params.put(key,parameterMap.get(key));
		}
		return params;
	}

	/**
	 * 获取单个请求的参数
	 * @param key 键
	 * @return 请求参数
	 */
	public String getParameter(String key) {
		return httpRequest.getParameter(key);
	}
	
	/**
	 * 获取单个请求的参数
	 * @param key 键
	 * @return 请求参数
	 */
	public String[] getParameterValues(String key) {
		return httpRequest.getParameterValues(key);
	}

	/**
	 * 获取请求的文件
	 * @return 文件列表
	 */
	public Map<String, MarsFileUpLoad> getFiles() throws Exception {
		return files;
	}

	/**
	 * 获取单个请求的文件
	 * 
	 * @param name 名称
	 * @return 单个文件
	 */
	public MarsFileUpLoad getFile(String name) throws Exception {
		if (files != null){
			return files.get(name);
		}
		return null;
	}
	
	/**
	 * 获取tomcat原生request
	 * @return 原生请求对象
	 */
	public HttpServletRequest getHttpServletRequest() {
		return httpRequest;
	}

	/**
	 * 获取Session
	 * @return jwt
	 */
	public HttpSession getSession(){
		return httpRequest.getSession();
	}
	
	/**
	 * 获取客户端IP
	 * @return ip
	 */
	public String getIp() {
        return httpRequest.getRemoteHost();
	}
}
