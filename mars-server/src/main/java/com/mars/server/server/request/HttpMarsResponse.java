package com.mars.server.server.request;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.base.config.model.CrossDomainConfig;
import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MarsConfiguration;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应对象，对netty原生response的扩展
 * <p>
 * 暂时没有提供response的支持
 *
 * @author yuye
 */
public class HttpMarsResponse {

    private Logger logger = LoggerFactory.getLogger(HttpMarsResponse.class);

    /**
     * java原生通道
     */
    private HttpExchange httpExchange;

    /**
     * 响应头
     */
    private Map<String, String> header;


    /**
     * 构造函数，框架自己用的，程序员用不到，用了也没意义
     *
     * @param httpExchange java原生通道
     */
    public HttpMarsResponse(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.header = new HashMap<>();
    }

    /**
     * 获取java原生httpExchange
     * @return java原生通道
     */
    public HttpExchange geHttpServletResponse() {
        return httpExchange;
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
        OutputStream out = null;
        try {
            crossDomain();
            loadHeader();

            /* 设置响应头，必须在sendResponseHeaders方法之前设置 */
            httpExchange.getResponseHeaders().add("Content-Type:", "text/json;charset="+MarsConstant.ENCODING);

            /* 设置响应码和响应体长度，必须在getResponseBody方法之前调用 */
            byte[] responseContentByte = context.getBytes(MarsConstant.ENCODING);
            httpExchange.sendResponseHeaders(200, responseContentByte.length);

            out = httpExchange.getResponseBody();
            out.write(responseContentByte);
        } catch (Exception e){
            logger.error("响应数据异常",e);
        } finally {
            if (out != null){
                try{
                    out.flush();
                    out.close();
                } catch (Exception e){
                }
            }
        }
    }

    /**
     * 文件下载
     * @param fileName
     * @param inputStream
     */
    public void downLoad(String fileName, InputStream inputStream) {
        OutputStream out = null;
        try {
            if(fileName == null || inputStream == null){
                logger.error("downLoad方法的传参不可以为空");
                return;
            }
            crossDomain();
            httpExchange.getResponseHeaders().add("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,MarsConstant.ENCODING));

            int len=0;
            byte[] buffer = new byte[1024];

            //设置响应码和响应体长度，必须在getResponseBody方法之前调用！
            httpExchange.sendResponseHeaders(200, inputStream.available());

            out = httpExchange.getResponseBody();
            while((len=inputStream.read(buffer))!=-1){
                out.write(buffer, 0, len);
            }
        } catch (Exception e){
            logger.error("响应数据异常",e);
        } finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (Exception e){
            }
        }
    }

    /**
     * 加载设置的header
     */
    private void loadHeader(){
        if (header != null && !header.isEmpty()) {
            for (String key : header.keySet()) {
                httpExchange.getResponseHeaders().set(key, header.get(key));
            }
        }
    }

    /**
     * 设置跨域
     */
    private void crossDomain() {
        MarsConfig marsConfig = MarsConfiguration.getConfig();
        CrossDomainConfig crossDomainConfig = marsConfig.crossDomainConfig();
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", crossDomainConfig.getOrigin());
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Methods", crossDomainConfig.getMethods());
        httpExchange.getResponseHeaders().set("Access-Control-Max-Age", crossDomainConfig.getMaxAge());
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Headers", crossDomainConfig.getHeaders());
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Credentials", crossDomainConfig.getCredentials());
    }
}
