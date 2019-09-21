package com.mars.server.server.request.model;

/**
 * 跨域配置
 */
public class CrossDomain {

    public static String origin = "*";
    public static String methods = "GET,POST";
    public static String maxAge = "9";
    public static String headers = "x-requested-with,Cache-Control,Pragma,Content-Type,Token";
    public static String credentials = "true";
}
