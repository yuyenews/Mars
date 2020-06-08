package com.mars.iserver.par;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.common.constant.MarsConstant;
import com.mars.iserver.par.formdata.ParsingFormData;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.model.MarsFileUpLoad;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpMarsRequest工厂
 */
public class HttpMarsRequestFactory {



    /**
     * 从httpExchange中提取出所有的参数，并放置到HttpMarsRequest中
     * @param httpExchange 请求
     * @param marsRequest mars请求
     * @return 加工后的mars请求
     * @throws Exception 异常
     */
    public static HttpMarsRequest getHttpMarsRequest(HttpExchange httpExchange, HttpMarsRequest marsRequest) throws Exception {
        Map<String, MarsFileUpLoad> files = new HashMap<>();
        Map<String, List<String>> marsParams = new HashMap<>();

        if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
            /* 从get请求中获取参数 */
            String paramStr = httpExchange.getRequestURI().getQuery();
            marsParams = urlencoded(paramStr, marsParams, false);
        } else {
            /* 从非GET请求中获取参数 */
            InputStream inputStream = httpExchange.getRequestBody();
            if (inputStream == null) {
                return marsRequest;
            }

            /* 根据提交方式，分别处理参数 */
            String contentType = getContentType(httpExchange);
            if (contentType.startsWith("application/x-www-form-urlencoded")) {
                /* 正常的表单提交 */
                String paramStr = getParamStr(inputStream);
                marsParams = urlencoded(paramStr, marsParams, true);
            } else if (contentType.startsWith("multipart/form-data")) {
                /* formData提交，可以用于文件上传 */
                Map<String, Object> result = formData(httpExchange, marsParams, files, contentType);
                files = (Map<String, MarsFileUpLoad>) result.get(ParsingFormData.FILES_KEY);
                marsParams = (Map<String, List<String>>) result.get(ParsingFormData.PARAMS_KEY);
            } else if (contentType.startsWith("application/json")) {
                /* RAW提交（json） */
                marsParams = raw(inputStream, marsParams);
            }
        }
        /* 将提取出来的参数，放置到HttpMarsRequest中 */
        marsRequest.setFiles(files);
        marsRequest.setParams(marsParams);

        return marsRequest;
    }

    /**
     * 获取提交方式
     * @param httpExchange 请求对象
     * @return 提交方式
     */
    private static String getContentType(HttpExchange httpExchange){
        List<String> ctList = httpExchange.getRequestHeaders().get("Content-type");
        if(ctList == null || ctList.size() < 1){
            return null;
        }
        return ctList.get(0).trim().toLowerCase();
    }

    /**
     * 从输入流里面读取所有的数据
     * @param inputStream 输入流
     * @return 数据
     * @throws Exception 异常
     */
    private static String getParamStr(InputStream inputStream) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, MarsConstant.ENCODING));
        String line = null;
        StringBuffer paramsStr = new StringBuffer();
        while ((line = br.readLine()) != null) {
            paramsStr.append(line);
        }
        return paramsStr.toString();
    }

    /**
     * 表单提交处理
     * @param paramStr 数据
     * @param marsParams httpMarsRequest的参数对象
     * @param hasDecode 是否需要解码 true是，false不是
     * @return httpMarsRequest的参数对象
     * @throws Exception 异常
     */
    private static Map<String,List<String>> urlencoded(String paramStr,Map<String,List<String>> marsParams, boolean hasDecode) throws Exception {
        if(paramStr != null){
            String[] paramsArray = paramStr.split("&");
            if(paramsArray == null || paramsArray.length < 1){
                return marsParams;
            }
            List<String> values = null;
            for(String paramItem : paramsArray){
                String[] param = paramItem.split("=");
                if(param == null || param.length < 2){
                    continue;
                }
                String key = param[0];

                values = marsParams.get(key);
                if(values == null){
                    values = new ArrayList<>();
                }

                String value = param[1];
                if(hasDecode){
                    value = URLDecoder.decode(value, MarsConstant.ENCODING);
                }
                values.add(value);
                marsParams.put(key,values);
            }
        }
        return marsParams;
    }

    /**
     * RAW提交处理
     * @param inputStream 输入流
     * @param marsParams httpMarsRequest的参数对象
     * @return httpMarsRequest的参数对象
     * @throws Exception 异常
     */
    private static Map<String,List<String>> raw(InputStream inputStream,Map<String,List<String>> marsParams) throws Exception {
        String paramStr = getParamStr(inputStream);
        if(paramStr == null || paramStr.trim().equals("")){
            return marsParams;
        }

        JSONObject jsonObject = JSONObject.parseObject(paramStr);
        List<String> values = null;
        for(String key : jsonObject.keySet()){
            values = marsParams.get(key);
            if(values == null){
                values = new ArrayList<>();
            }
            Object val = jsonObject.get(key);
            if(val != null){
                if(val instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray)val;
                    for(int i = 0; i<jsonArray.size(); i++){
                        values.add(jsonArray.getString(i));
                    }
                } else {
                    values.add(val.toString());
                }
            }
            marsParams.put(key,values);
        }
        return marsParams;
    }

    /**
     * formData提交处理
     * @param exchange 请求对象
     * @param marsParams httpMarsRequest的参数对象
     * @param files httpMarsRequest的文件参数对象
     * @param contentType 内容类型
     * @return httpMarsRequest的参数对象 和 httpMarsRequest的文件参数对象
     * @throws Exception 异常
     */
    private static Map<String,Object> formData(HttpExchange exchange, Map<String,List<String>> marsParams, Map<String, MarsFileUpLoad> files, String contentType) throws Exception {
        return ParsingFormData.parsing(exchange,marsParams,files,contentType);
    }
}
