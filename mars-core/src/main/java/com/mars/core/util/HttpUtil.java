package com.mars.core.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;

/**
 * HTTP工具类
 */
public class HttpUtil {

    /**
     * 发起get请求
     * @param strUrl 链接
     * @param params 参数
     * @return 响应结果
     */
    public static Object get(String strUrl, Map<String,String> params) throws Exception {
        String url = strUrl+"?"+getParams(params);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }


    /**
     * 组装参数
     * @param params
     * @return
     */
    private static String getParams(Map<String,String> params){
        StringBuffer stringBuffer = new StringBuffer();
        if(params != null){
            for(String key : params.keySet()){
                stringBuffer.append(key);
                stringBuffer.append("=");
                stringBuffer.append(params.get(key));
                stringBuffer.append("&");
            }
        }
        return stringBuffer.substring(0,stringBuffer.length()-1);
    }

}
