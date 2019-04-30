package com.mars.core.util;

import com.mars.core.constant.MarsCloudConstant;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP工具类
 */
public class HttpUtil {

    /**
     * 发起请求，以序列化方式传递数据
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String request(String url, Object params) throws Exception {

        /* 将参数序列化成byte[] */
        byte[] param = SerializableUtil.serialization(params);

        OkHttpClient okHttpClient = getOkHttpClient();

        /* 发起post请求 将数据传递过去 */
        MediaType formData = MediaType.parse("multipart/form-data");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),param);
        MultipartBody body = new MultipartBody.Builder()
                .setType(formData)
                .addFormDataPart(MarsCloudConstant.PARAM,"params",fileBody)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }

    /**
     * 发起post请求
     *
     * @param url  请求链接
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String,Object> params) throws Exception {
        OkHttpClient okHttpClient = getOkHttpClient();

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        for(String key : params.keySet()){
            formBodyBuilder.add(key,params.get(key).toString());
        }

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发起get请求
     * @param strUrl 链接
     * @param params 参数
     * @return 响应结果
     */
    public static Object get(String strUrl, Map<String,String> params) throws Exception {
        String url = strUrl+"?"+getParams(params);
        OkHttpClient okHttpClient = getOkHttpClient();
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

    /**
     * 获取Okhttp客户端
     * @return
     * @throws Exception
     */
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30000, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        return okHttpClient;
    }
}
