package com.mars.aio.server.model;

/**
 * 请求地址
 */
public class RequestURI {

    /**
     * 请求地址
     */
    private String uri;

    public RequestURI(String uri){
        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.uri;
    }

    /**
     * 返回地址栏的参数
     * @return
     */
    public String getQuery(){
        if(this.uri.indexOf("?") < 0){
            return null;
        }
        return this.uri.substring(uri.indexOf("?") + 1);
    }
}
