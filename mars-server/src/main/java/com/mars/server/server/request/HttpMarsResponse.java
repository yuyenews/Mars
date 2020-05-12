package com.mars.server.server.request;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.base.config.model.CrossDomainConfig;
import com.mars.common.util.MarsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
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
     * netty原生通道
     */
    private HttpServletResponse response;

    /**
     * 响应头
     */
    private Map<String, String> header;


    /**
     * 构造函数，框架自己用的，程序员用不到，用了也没意义
     *
     * @param response netty原生通道
     */
    public HttpMarsResponse(HttpServletResponse response) {
        this.response = response;
        this.header = new HashMap<>();
    }

    /**
     * 获取tomcat原生response
     * @return netty原生通道
     */
    public HttpServletResponse geHttpServletResponse() {
        return response;
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
        PrintWriter out = null;
        try {
            crossDomain();
            loadHeader();

            response.setContentType("text/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(context);
        } catch (Exception e){
            logger.error("响应数据异常",e);
        } finally {
            if (out != null){
                out.flush();
                out.close();
            }
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
            crossDomain();
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,"UTF-8"));

            int len=0;
            byte[] buffer = new byte[1024];
            ServletOutputStream out = response.getOutputStream();
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
                response.setHeader(key, header.get(key));
            }
        }
    }

    /**
     * 设置跨域
     */
    private void crossDomain() {
        MarsConfig marsConfig = MarsConfiguration.getConfig();
        CrossDomainConfig crossDomainConfig = marsConfig.crossDomainConfig();
        response.setHeader("Access-Control-Allow-Origin", crossDomainConfig.getOrigin());
        response.setHeader("Access-Control-Allow-Methods", crossDomainConfig.getMethods());
        response.setHeader("Access-Control-Max-Age", crossDomainConfig.getMaxAge());
        response.setHeader("Access-Control-Allow-Headers", crossDomainConfig.getHeaders());
        response.setHeader("Access-Control-Allow-Credentials", crossDomainConfig.getCredentials());
    }
}
