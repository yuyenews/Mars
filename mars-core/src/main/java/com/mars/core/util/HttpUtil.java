package com.mars.core.util;


import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP工具类
 */
public class HttpUtil {

    /**
     * 发起请求，以序列化方式传递数据
     * @param url 路径
     * @param value 请求参数
     * @param name 参数name
     * @param timeOut 超时时间
     * @return 相应数据
     * @throws Exception
     */
    public static String request(String url, String name, byte[] value, long timeOut) throws Exception {

        OkHttpClient okHttpClient = getOkHttpClient(timeOut);

        /* 发起post请求 将数据传递过去 */
        MediaType formData = MediaType.parse("multipart/form-data");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),value);
        MultipartBody body = new MultipartBody.Builder()
                .setType(formData)
                .addFormDataPart(name,"params",fileBody)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        return okCall(okHttpClient,request);
    }

    /**
     * 发起post请求
     *
     * @param url  请求链接
     * @param params 参数
     * @param timeOut 超时时间
     * @return 相应数据
     * @throws Exception
     */
    public static String post(String url, Map<String,Object> params,long timeOut) throws Exception {
        OkHttpClient okHttpClient = getOkHttpClient(timeOut);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        for(String key : params.keySet()){
            formBodyBuilder.add(key,params.get(key).toString());
        }

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody).build();

        return okCall(okHttpClient,request);
    }

    /**
     * 发起get请求
     * @param strUrl 链接
     * @param params 参数
     * @param timeOut
     * @return 响应结果
     *
     * @throws Exception
     */
    public static Object get(String strUrl, Map<String,String> params,long timeOut) throws Exception {
        String url = strUrl+"?"+getParams(params);
        OkHttpClient okHttpClient = getOkHttpClient(timeOut);
        Request request = new Request.Builder()
                .url(url)
                .build();

        return okCall(okHttpClient,request);
    }


    /**
     * 开始请求
     * @param okHttpClient
     * @param request
     * @return 相应数据
     * @throws Exception
     */
    private static String okCall(OkHttpClient okHttpClient,Request request) throws Exception {
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        int code = response.code();
        ResponseBody responseBody = response.body();
        if(code != 200){
            throw new Exception("请求接口出现异常:"+responseBody.string());
        }
        return responseBody.string();
    }

    /**
     * 组装参数
     * @param params 参数
     * @return 相应数据
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

    /**
     * 获取okhttp客户端
     * @param timeOut
     * @return 客户端
     * @throws Exception
     */
    private static OkHttpClient getOkHttpClient(long timeOut) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(timeOut, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        return okHttpClient;
    }
}
