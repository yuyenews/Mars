package com.mars.server.server.request.model;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;

/**
 * 跨域配置
 */
public class CrossDomain {

    private static CrossDomain crossDomain;

    private String origin = "*";
    private String methods = "GET,POST";
    private String maxAge = "9";
    private String headers = "x-requested-with,Cache-Control,Pragma,Content-Type,Token";
    private String credentials = "true";

    private CrossDomain(){}

    public static CrossDomain getCrossDomain(){
        if(crossDomain == null){
            crossDomain = new CrossDomain();
            crossDomain.init();
        }
        return crossDomain;
    }

    /**
     * 初始化跨域配置
     */
    private void init(){
        JSONObject object = getConfig();
        if(object == null){
            return;
        }
        String origin2 = object.getString("origin");
        if(origin2 != null){
            this.origin = origin2;
        }
        String methods2 = object.getString("methods");
        if(methods2 != null){
            this.methods = methods2;
        }
        String maxAge2 = object.getString("maxAge");
        if(maxAge2 != null){
            this.maxAge = maxAge2;
        }
        String headers2 = object.getString("headers");
        if(headers2 != null){
            this.headers = headers2;
        }
        String credentials2 = object.getString("credentials");
        if(credentials2 != null){
            this.credentials = credentials2;
        }
    }

    /**
     * 获取配置文件
     *
     * @return 配置文件对象
     */
    private static JSONObject getConfig() {
        try {
            return ConfigUtil.getConfig().getJSONObject("cross_domain");
        } catch (Exception e){
            // 不需要做任何处理
        }
        return new JSONObject();
    }

    public String getOrigin() {
        return origin;
    }

    public String getMethods() {
        return methods;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public String getHeaders() {
        return headers;
    }

    public String getCredentials() {
        return credentials;
    }
}
