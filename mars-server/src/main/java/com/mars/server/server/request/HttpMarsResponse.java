package com.mars.server.server.request;

import com.mars.server.server.request.model.CrossDomain;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
            // 不处理
        } finally {
            if (out != null){
                out.flush();
                out.close();
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
        CrossDomain crossDomain = CrossDomain.getCrossDomain();
        response.setHeader("Access-Control-Allow-Origin", crossDomain.getOrigin());
        response.setHeader("Access-Control-Allow-Methods", crossDomain.getMethods());
        response.setHeader("Access-Control-Max-Age", crossDomain.getMaxAge());
        response.setHeader("Access-Control-Allow-Headers", crossDomain.getHeaders());
        response.setHeader("Access-Control-Allow-Credentials", crossDomain.getCredentials());
    }
}
