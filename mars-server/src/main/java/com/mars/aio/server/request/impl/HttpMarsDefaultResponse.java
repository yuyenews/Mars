package com.mars.aio.server.request.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.aio.constant.HttpConstant;
import com.mars.server.http.request.MartianHttpExchange;
import com.mars.aio.server.request.HttpMarsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应对象，对原生response的扩展
 * <p>
 * 暂时没有提供response的支持
 *
 * @author yuye
 */
public class HttpMarsDefaultResponse extends HttpMarsResponse {

    private Logger logger = LoggerFactory.getLogger(HttpMarsDefaultResponse.class);

    /**
     * java原生通道
     */
    private MartianHttpExchange httpExchange;

    /**
     * 响应头
     */
    private Map<String, String> header;


    /**
     * 构造函数，框架自己用的，程序员用不到，用了也没意义
     *
     * @param httpExchange java原生通道
     */
    public HttpMarsDefaultResponse(MartianHttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.header = new HashMap<>();
    }

    /**
     * 获取java原生httpExchange
     * @return java原生通道
     */
    public <T> T geNativeResponse(Class<T> cls) {
        return (T)httpExchange;
    }

    /**
     * 设置响应头
     *
     * @param key   键
     * @param value 值
     */
    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    /**
     * 响应数据
     *
     * @param context 消息
     */
    public void send(String context) {
        try {
            loadHeader();

            /* 设置响应头，必须在sendResponseHeaders方法之前设置 */
            httpExchange.setResponseHeader(MarsConstant.CONTENT_TYPE, HttpConstant.RESPONSE_CONTENT_TYPE);

            /* 设置响应码和响应体长度，必须在getResponseBody方法之前调用 */
            httpExchange.sendText(200, context);
        } catch (Exception e){
            logger.error("响应数据异常",e);
        }
    }

    /**
     * 文件下载
     * @param fileName
     * @param inputStream
     */
    public void downLoad(String fileName, InputStream inputStream) {
        try {
            if(fileName == null || inputStream == null){
                logger.error("downLoad方法的传参不可以为空");
                return;
            }
            httpExchange.setResponseHeader(MarsConstant.CONTENT_DISPOSITION, "attachment; filename="+ URLEncoder.encode(fileName,MarsConstant.ENCODING));

            /* 设置要下载的文件 */
            httpExchange.setResponseBody(inputStream);

        } catch (Exception e){
            logger.error("响应数据异常",e);
        }
    }

    /**
     * 加载设置的header
     */
    private void loadHeader(){
        if (header != null && !header.isEmpty()) {
            for (String key : header.keySet()) {
                httpExchange.setResponseHeader(key, header.get(key));
            }
        }
    }
}
