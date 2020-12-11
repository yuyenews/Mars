package com.mars.iserver.server.model;

public class RequestURI {

    private String uri;

    public RequestURI(String uri){
        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.uri;
    }

    public String getQuery(){
        if(this.uri.indexOf("?") < 0){
            return null;
        }
        return this.uri.substring(uri.indexOf("?") + 1);
    }
}
