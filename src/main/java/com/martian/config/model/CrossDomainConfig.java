package com.martian.config.model;

/**
 * 跨域配置
 */
public class CrossDomainConfig {

    private String origin = "*";
    private String methods = "GET,POST,PUT,DELETE,OPTIONS";
    private String maxAge = "9";
    private String headers = "x-requested-with,Cache-Control,Pragma,Content-Type,Token";
    private String credentials = "true";

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}

