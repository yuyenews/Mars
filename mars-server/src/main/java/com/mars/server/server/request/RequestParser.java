package com.mars.server.server.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.mars.core.constant.MarsConstant;
import com.mars.server.server.request.model.MarsFileUpLoad;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;

/**
 * 参数解析器
 * 
 * @author yuye
 *
 */
public class RequestParser {

	private FullHttpRequest fullReq;

	/**
	 * 构造一个解析器
	 * 
	 * @param req 请求对象
	 */
	public RequestParser(FullHttpRequest req) {
		this.fullReq = req;
	}

	/**
	 * 解析请求参数
	 * 
	 * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
	 *
	 * @throws Exception 异常
	 */
	public Map<String, Object> parse() throws Exception {
		HttpMethod method = fullReq.method();

		Map<String, Object> parmMap = new HashMap<>();

		if (HttpMethod.GET == method) {
			// 是GET请求
			QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
			Map<String, List<String>> params = decoder.parameters();
			for(String key : params.keySet()){
				parmMap.put(key, params.get(key));
			}
		} else if (HttpMethod.POST == method) {
			// 是POST请求
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
			decoder.offer(fullReq);

			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

			Map<String, MarsFileUpLoad> files = new Hashtable<>();

			for (InterfaceHttpData paramListItem : parmList) {
				if (paramListItem instanceof Attribute) {
					parmMap = setAttr(paramListItem,parmMap);
				} else if (paramListItem instanceof FileUpload) {
					files = setFile(paramListItem,files);
				}
			}
			parmMap.put(MarsConstant.REQUEST_FILE, files);
		}

		return parmMap;
	}

	/**
	 * 获取常规参数
	 * @param paramListItem
	 * @param parmMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> setAttr(InterfaceHttpData paramListItem,Map<String, Object> parmMap) throws Exception {
		Attribute data = (Attribute) paramListItem;
		List<Object> params = null;
		Object paramItem = parmMap.get(data.getName());
		if (paramItem == null) {
			params = new ArrayList<>();
		} else {
			params = (List<Object>) paramItem;
		}
		params.add(data.getValue());
		parmMap.put(data.getName(), params);

		return parmMap;
	}

	/**
	 * 获取文件参数
	 * @param paramListItem
	 * @param files
	 * @return
	 * @throws Exception
	 */
	private Map<String, MarsFileUpLoad> setFile(InterfaceHttpData paramListItem,Map<String, MarsFileUpLoad> files) throws Exception {
		FileUpload fileUpload = (FileUpload) paramListItem;

		byte[] bs = fileUpload.get();

		InputStream inputStream = new ByteArrayInputStream(bs);

		MarsFileUpLoad upLoad = new MarsFileUpLoad();
		upLoad.setFileName(fileUpload.getFilename());
		upLoad.setInputStream(inputStream);
		upLoad.setName(fileUpload.getName());
		upLoad.setBytes(bs);

		files.put(fileUpload.getName(), upLoad);

		return files;
	}
}
