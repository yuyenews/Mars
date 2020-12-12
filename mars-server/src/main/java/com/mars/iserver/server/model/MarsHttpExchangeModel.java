package com.mars.iserver.server.model;

import com.mars.common.constant.MarsConstant;

import java.io.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class MarsHttpExchangeModel {

    protected SelectionKey selectionKey;

    protected Selector selector;

    protected SocketChannel socketChannel;

    protected RequestURI requestURI;

    protected InputStream requestBody;

    protected String sendText;

    protected OutputStream responseBody;

    protected HttpHeaders requestHeaders;

    protected HttpHeaders responseHeaders;

    protected String requestMethod;

    protected String httpVersion;

    protected int statusCode;

    public MarsHttpExchangeModel(){
        requestHeaders = new HttpHeaders();
        responseHeaders = new HttpHeaders();
        List<String> values = new ArrayList<>();
        values.add("text/json;charset="+ MarsConstant.ENCODING);
        responseHeaders.put("Content-Type", values);

        responseBody = new ByteArrayOutputStream();
        sendText = "没有响应任何数据";
        statusCode = 200;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public RequestURI getRequestURI() {
        return requestURI;
    }

    public OutputStream getResponseBody() {
        return responseBody;
    }

    public void addRequestHeader(String name, String value){
        List<String> values = requestHeaders.get(name);
        if(values == null){
            values = new ArrayList<>();
        }

        values.add(value);
        requestHeaders.put(name, values);
    }

    public void setRequestHeader(String name, String value){
        List<String> values = new ArrayList<>();
        values.add(value);
        requestHeaders.put(name, values);
    }

    public void addResponseHeader(String name, String value){
        List<String> values = responseHeaders.get(name);
        if(values == null){
            values = new ArrayList<>();
        }

        values.add(value);
        responseHeaders.put(name, values);
    }

    public void setResponseHeader(String name, String value){
        List<String> values = new ArrayList<>();
        values.add(value);
        responseHeaders.put(name, values);
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public InputStream getRequestBody() {
        return requestBody;
    }
}
