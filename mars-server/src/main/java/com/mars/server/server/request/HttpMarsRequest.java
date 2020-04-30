package com.mars.server.server.request;

import com.mars.server.server.request.model.MarsFileUpLoad;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
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
	 * 参数
	 */
	private Map<String,List<String>> marsParams;

	/**
	 * 上传的文件
	 */
	private Map<String, MarsFileUpLoad> files;

	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpRequest
	 */
	public HttpMarsRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	/**
	 * 添加上传的文件
	 * @param files
	 */
	public void setFiles(Map<String,MarsFileUpLoad> files){
		this.files = files;
	}

	/**
	 * 上传文件时添加参数
	 * @param params
	 */
	public void setParams(Map<String, List<String>> params) {
		if(params == null || params.size() < 1){
			return;
		}
		this.marsParams = params;
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
		if(ServletFileUpload.isMultipartContent(httpRequest)){
			if(marsParams == null){
				return params;
			}
			for(String key : marsParams.keySet()){
				List<String> paramsList = marsParams.get(key);
				if(paramsList == null || paramsList.size() < 1){
					continue;
				}
				String[] paramsListToArray = paramsListToArray(paramsList);
				if(paramsListToArray != null && paramsListToArray.length == 1){
					params.put(key,paramsListToArray[0]);
				} else {
					params.put(key,paramsListToArray);
				}
			}
		} else {
			Map<String,String[]> parameterMap = httpRequest.getParameterMap();
			for(String key : parameterMap.keySet()){
				String[] paramsListToArray = parameterMap.get(key);
				if(paramsListToArray != null && paramsListToArray.length == 1){
					params.put(key,paramsListToArray[0]);
				} else {
					params.put(key,paramsListToArray);
				}

			}
		}
		return params;
	}

	/**
	 * 获取单个请求的参数
	 * @param key 键
	 * @return 请求参数
	 */
	public String getParameter(String key) {
		if(ServletFileUpload.isMultipartContent(httpRequest)){
			if(marsParams != null){
				List<String> marsParam = marsParams.get(key);
				if(marsParam == null){
					return null;
				}
				return marsParam.get(0);
			}
		} else {
			return httpRequest.getParameter(key);
		}
		return null;
	}
	
	/**
	 * 获取单个请求的参数
	 * @param key 键
	 * @return 请求参数
	 */
	public String[] getParameterValues(String key) {
		if(ServletFileUpload.isMultipartContent(httpRequest)){
			if(marsParams != null){
				List<String> paramsList = marsParams.get(key);
				return paramsListToArray(paramsList);
			}
		} else {
			return httpRequest.getParameterValues(key);
		}
		return null;
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
	public String getRemoteHost() {
        return httpRequest.getRemoteHost();
	}

	/**
	 * 参数集合转String[]
	 * @param paramsList
	 * @return
	 */
	private String[] paramsListToArray(List<String> paramsList){
		if(paramsList == null || paramsList.size() < 1){
			return null;
		}
		String[] paramsArray = new String[paramsList.size()];
		return paramsList.toArray(paramsArray);
	}
}
