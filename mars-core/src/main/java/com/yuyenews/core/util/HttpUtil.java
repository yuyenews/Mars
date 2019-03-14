package com.yuyenews.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HTTP工具类
 */
public class HttpUtil {

    /**
     * 发起post请求
     * @param url 链接
     * @param params 参数
     * @return 响应结果
     */
    public static Object post(String url, Map<String,String> params) throws Exception {
        return request(url,params,"POST");
    }

    /**
     * 发起get请求
     * @param url 链接
     * @param params 参数
     * @return 响应结果
     */
    public static Object get(String url, Map<String,String> params) throws Exception {
        return request(url,params,"GET");
    }

    /**
     * 发起请求
     * @param strUrl 链接
     * @param params 参数
     * @param method 请求方式
     * @return 响应结果
     */
    public static Object request(String strUrl, Map<String,String> params,String method) throws Exception{

        try {
            URL url = new URL(strUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod(method);
            if(params != null){
                for(String key : params.keySet()){
                    httpConn.setRequestProperty(key,params.get(key));
                }
            }
            InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "UTF-8");
            BufferedReader bufReader = new BufferedReader(input);
            String line = "";
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}
